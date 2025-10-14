package com.yarmook.realstate.config;

import com.yarmook.realstate.domain.Authority;
import com.yarmook.realstate.domain.User;
import com.yarmook.realstate.repository.AuthorityRepository;
import com.yarmook.realstate.repository.UserRepository;
import com.yarmook.realstate.security.AuthoritiesConstants;
import com.yarmook.realstate.service.SequenceGeneratorService;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MongoDataInitializer implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDataInitializer.class);

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SequenceGeneratorService sequenceGeneratorService;

    public MongoDataInitializer(
        AuthorityRepository authorityRepository,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        SequenceGeneratorService sequenceGeneratorService
    ) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public void run(String... args) {
        ensureAuthority(AuthoritiesConstants.ADMIN);
        ensureAuthority(AuthoritiesConstants.AGENT);
        ensureAuthority(AuthoritiesConstants.VISITOR);

        userRepository
            .findOneByLogin("admin")
            .orElseGet(() -> {
                LOG.info("Creating default admin user");
                User admin = new User();
                admin.setId(sequenceGeneratorService.generateSequence(User.SEQUENCE_NAME));
                admin.setLogin("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setEmail("admin@localhost");
                admin.setLangKey("en");
                admin.setActivated(true);
                admin.setCreatedBy("system");
                admin.setCreatedDate(Instant.now());
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById(AuthoritiesConstants.ADMIN).ifPresent(authorities::add);
                authorityRepository.findById(AuthoritiesConstants.AGENT).ifPresent(authorities::add);
                admin.setAuthorities(authorities);
                return userRepository.save(admin);
            });
    }

    private void ensureAuthority(String name) {
        authorityRepository.findById(name).orElseGet(() -> authorityRepository.save(createAuthority(name)));
    }

    private Authority createAuthority(String name) {
        Authority authority = new Authority();
        authority.setName(name);
        return authority;
    }
}
