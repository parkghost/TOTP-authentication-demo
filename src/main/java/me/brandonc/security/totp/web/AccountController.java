package me.brandonc.security.totp.web;

import static me.brandonc.security.totp.util.PasswordUtils.encrypt;
import static me.brandonc.security.totp.util.TOTPUtils.checkCode;
import static me.brandonc.security.totp.util.TOTPUtils.generateSecret;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Hashtable;

import javax.servlet.http.HttpServletResponse;

import me.brandonc.security.totp.domain.Account;
import me.brandonc.security.totp.repository.AccountRepository;
import me.brandonc.security.totp.repository.QRCodeTicketRepository;
import me.brandonc.security.totp.web.response.ApplicationStatus;
import me.brandonc.security.totp.web.response.ResponseMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

	private final Logger logger = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountRepository acccountRepository;

	@Autowired
	private QRCodeTicketRepository qrCodeTicketRepository;

	@Value("${qrcode.width}")
	private int qrcodeWidth = 400;

	@Value("${qrcode.height}")
	private int qrcodeHeight = 400;

	@Value("${totp.intervalPeriod}")
	private int intervalPeriod = 30;

	@Value("${totp.window}")
	private int window = 3;

	@Value("${totp.hostLabel}")
	private String hostLabel = "brandonc.me";

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {

		dataBinder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				if (text == null) {
					return;
				}
				setValue(text.replaceAll("<.[^<>]*?>", ""));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return (value != null ? value.toString() : "");
			}
		});

	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage create(String name, String password) {
		if (acccountRepository.exist(name)) {
			return new ResponseMessage(ApplicationStatus.EXISTED, "The account already exist");
		}

		Account account = new Account();
		account.setName(name);
		account.setPassword(encrypt(password, name));
		account.setSecret(generateSecret());
		account.setCreated(new Date());
		acccountRepository.addAccount(account);

		qrCodeTicketRepository.createTicket(name);

		return ResponseMessage.SUCCESSED;

	}

	@RequestMapping(value = "{name}/verify", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage verifyCode(@PathVariable String name, @RequestParam String password, @RequestParam long code) throws InvalidKeyException, NoSuchAlgorithmException {
		Account account = acccountRepository.findAccountByName(name);

		if (account != null && checkPassword(account, password) && checkCode(account.getSecret(), code, getCurrentInterval(), window)) {
			return ResponseMessage.SUCCESSED;
		}

		return new ResponseMessage(ApplicationStatus.FAILED, "Verification failed");
	}

	@RequestMapping(value = "{name}/qrcode", method = RequestMethod.GET)
	public void showQrcode(@PathVariable String name, HttpServletResponse response) throws IllegalAccessException {

		if (!qrCodeTicketRepository.useTicket(name)) {
			throw new IllegalAccessException("no permission");
		}

		Account account = acccountRepository.findAccountByName(name);

		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");

		String data = getQRBarcodeURL(account.getName(), hostLabel, account.getSecret());

		BitMatrix matrix = null;
		com.google.zxing.Writer writer = new MultiFormatWriter();
		try {
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, qrcodeWidth, qrcodeHeight, hints);
		} catch (com.google.zxing.WriterException e) {
			logger.error(e.getMessage());
		}

		try {
			MatrixToImageWriter.writeToStream(matrix, "PNG", response.getOutputStream());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	private boolean checkPassword(Account account, String password) throws NoSuchAlgorithmException, InvalidKeyException {
		return account.getPassword().equals(encrypt(password, account.getName()));
	}

	private long getCurrentInterval() {
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		return currentTimeSeconds / intervalPeriod;
	}

	private String getQRBarcodeURL(String user, String host, String secret) {
		String format = "otpauth://totp/%s@%s?secret=%s";
		return String.format(format, user, host, secret);
	}

}
