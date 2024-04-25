package uk.mirek.kpler.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.mirek.kpler.models.UserRateLimitModel;
import uk.mirek.kpler.repositories.UserRateLimitRepo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRateLimitRepo userRateLimitRepo;

    @InjectMocks
    private UserService service;

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(userRateLimitRepo);
    }

    @Test
    void shouldRegisterCall() {
        var uid = "Cthulhu";
        service.registerRequest(uid);

        var captor = ArgumentCaptor.forClass(UserRateLimitModel.class);
        verify(userRateLimitRepo).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getUid(), is(uid));
    }

    @Test
    void shouldRegisterFreeCall() {
        service.registerRequest(null);

        var captor = ArgumentCaptor.forClass(UserRateLimitModel.class);
        verify(userRateLimitRepo).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getUid(), is("FREE_ACCESS"));
    }

    @Test
    void shouldAllowAccess() {
        var uid = "Cthulhu";
        when(userRateLimitRepo.countRequests(eq(uid), anyLong())).thenReturn(0);
        var isLimited = service.isRateLimited(uid);
        assertThat(isLimited, is(false));

        verify(userRateLimitRepo).countRequests(eq(uid), anyLong());
    }
}