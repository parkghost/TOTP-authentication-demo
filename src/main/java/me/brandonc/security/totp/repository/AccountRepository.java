package me.brandonc.security.totp.repository;

import me.brandonc.security.totp.domain.Account;

public interface AccountRepository {

	void addAccount(Account account);

	Account findAccountById(long id);

	Account findAccountByName(String name);

	boolean exist(String name);
}
