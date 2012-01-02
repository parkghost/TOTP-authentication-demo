package me.brandonc.security.totp.repository;

import static me.brandonc.security.totp.util.KeyFormatUtils.getAccountDataKey;
import static me.brandonc.security.totp.util.KeyFormatUtils.getAccountNameIndexKey;
import static me.brandonc.security.totp.util.KeyFormatUtils.getGlobalIdKey;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.brandonc.security.totp.domain.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	@Autowired
	private JedisPool pool;

	@Override
	public void addAccount(Account account) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();

			long id = getIncresedId();

			Map<String, String> hash = new HashMap<String, String>();
			hash.put("name", account.getName());
			hash.put("password", account.getPassword());
			hash.put("secret", account.getSecret());
			hash.put("created", Long.toString(account.getCreated().getTime()));

			jedis.set(getAccountNameIndexKey(account.getName()), Long.toString(id));
			jedis.hmset(getAccountDataKey(Long.toString(id)), hash);
		} finally {
			pool.returnResource(jedis);
		}

	}

	@Override
	public Account findAccountById(long id) {

		Account account = null;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();

			if (id > 0) {
				List<String> item = jedis.hmget(getAccountDataKey(Long.toString(id)), "name", "password", "secret", "created");
				if (item != null) {
					account = new Account();
					account.setName(item.get(0));
					account.setPassword(item.get(1));
					account.setSecret(item.get(2));
					account.setCreated(new Date(Long.parseLong(item.get(3))));
				}
			}
		} finally {
			pool.returnResource(jedis);
		}

		return account;
	}

	@Override
	public Account findAccountByName(String name) {

		Account account = null;
		Jedis jedis = null;
		try {
			jedis = pool.getResource();

			String id = jedis.get(getAccountNameIndexKey(name));

			if (id != null) {
				account = findAccountById(Long.parseLong(id));
			}
		} finally {
			pool.returnResource(jedis);
		}

		return account;
	}

	private long getIncresedId() {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();

			return jedis.incr(getGlobalIdKey());
		} finally {
			pool.returnResource(jedis);
		}
	}

	@Override
	public boolean exist(String name) {

		Jedis jedis = null;
		try {
			jedis = pool.getResource();

			String id = jedis.get(getAccountNameIndexKey(name));

			if (id != null) {
				return true;
			}
		} finally {
			pool.returnResource(jedis);
		}

		return false;
	}
}
