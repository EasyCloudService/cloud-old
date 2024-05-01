package net.easycloud.wrapper.classloader;

import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

@Getter
public final class ApplicationExternalClassLoader extends URLClassLoader {

    private boolean closed = false;

    public ApplicationExternalClassLoader(final URL url) {
        super(new URL[]{url}, ClassLoader.getSystemClassLoader());
    }

    @Override
    public void close() throws IOException {
        this.closed = true;
        super.close();
    }
}
