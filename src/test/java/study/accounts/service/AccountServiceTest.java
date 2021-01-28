package study.accounts.service;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import study.accounts.domain.Account;
import study.accounts.domain.AccountRepository;
import study.accounts.domain.AccountRole;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void findByUserName() {
        String userName = "jinSeok@test.com";
        String password = "jinSeok";

        // Given
        Account account = Account.builder()
                .email(userName)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountRepository.save(account);

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        // Then
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUserNameFail() {
        String userName = "random@test.com";

        try {
            accountService.loadUserByUsername(userName);
            fail("supposed to be failed");
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence(userName);
        }

    }

}
