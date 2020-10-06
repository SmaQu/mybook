package com.alastor.mybook;

import androidx.test.filters.SmallTest;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SmallTest
public class ResponseTest {

    @Test
    public void check_is_status_loading() {
        Response<Void> response = Response.loading();
        assertThat(response.status, is(equalTo(Response.Status.LOADING)));
    }

    @Test
    public void check_is_status_error_with_IOException() {
        Response<Void> response = Response.error(new IOException());
        assertThat(response.status, is(equalTo(Response.Status.ERROR)));
        assertThat(response.error, instanceOf(IOException.class));
    }

    @Test
    public void check_is_status_success_with_string() {
        final String responseString = "response";
        Response<String> response = Response.success(responseString);
        assertThat(response.status, is(equalTo(Response.Status.SUCCESS)));
        assertThat(response.data, is(equalTo(responseString)));
    }
}
