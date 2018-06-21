package main.java.com.buildsim.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class SelfDestroyFile extends File {
    private static final long serialVersionUID = 1;
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private boolean includeParent;

    public SelfDestroyFile(String pathName, boolean includeParent) {
        super(pathName);
        this.includeParent = includeParent;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.delete();
            if (includeParent) {
                File parentFolder = this.getParentFile();
                if (parentFolder != null) {
                    parentFolder.delete();
                }
            }
        } catch (Exception e) {
            LOG.warn("Delete SelfDestroyFile encounters error, " + e.getMessage(), e);
        } finally {
            super.finalize();
        }
    }
}
