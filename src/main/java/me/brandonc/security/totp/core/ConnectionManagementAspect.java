package me.brandonc.security.totp.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;

/**
 * make one thread use one connection in the same time
 */
@Aspect
public class ConnectionManagementAspect {

	private final Logger logger = LoggerFactory.getLogger(ConnectionManagementAspect.class);

	ThreadLocal<Object> localConnection = new NamedThreadLocal<Object>("localConnection");
	ThreadLocal<Integer> localEntrend = new NamedThreadLocal<Integer>("localEntrend") {
		@Override
		protected Integer initialValue() {
			return 0;
		}
	};

	@Pointcut("within(me.brandonc.security.totp.repository.*)")
	public void inRepository() {
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	@Around("call(* redis.clients.util.Pool.getResource()) && inRepository()")
	public Object getResource(ProceedingJoinPoint pjp) {
		Object connection = localConnection.get();
		if (connection == null) {
			logger.trace("create resource");
			try {
				connection = pjp.proceed();
				localConnection.set(connection);

			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			logger.trace("use cached resource");
		}

		localEntrend.set(localEntrend.get().intValue() + 1);
		return connection;
	}

	@SuppressAjWarnings("adviceDidNotMatch")
	@Around("call(* redis.clients.util.Pool.returnResource(..)) && args(resource) && inRepository()")
	public void releaseResource(ProceedingJoinPoint pjp, Object resource) {

		int entrend = localEntrend.get().intValue();
		if (--entrend == 0) {
			try {
				logger.trace("remove cached resource");
				pjp.proceed(new Object[] { resource });
				localConnection.set(null);
				localEntrend.set(0);
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		} else {
			localEntrend.set(entrend);
		}
	}
}
