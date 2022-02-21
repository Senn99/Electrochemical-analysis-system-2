package bluedot.electrochemistry.commons.factory;

import bluedot.electrochemistry.cache.entity.FileData;
import bluedot.electrochemistry.cache.local.CodeCache;
import bluedot.electrochemistry.cache.local.FileDataCache;
import bluedot.electrochemistry.cache.local.StringArrayCache;
import bluedot.electrochemistry.cache.local.StringCache;
import bluedot.electrochemistry.commons.Lifecycle;
import bluedot.electrochemistry.commons.dao.BaseMapper;
import bluedot.electrochemistry.commons.entity.EaFile;
import bluedot.electrochemistry.simplespring.core.BeanContainer;
import com.google.common.cache.CacheLoader;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author Senn
 * @create 2022/2/5 19:30
 */
public class CacheExecutorFactory implements Lifecycle {

    private String fileRootPath;

    public static FileDataCache createFileCache() {
        return FileDataCache.getInstance();
    }

    public static StringArrayCache createStringArrayCache() {
        return StringArrayCache.getInstance();
    }

    public static CodeCache createCodeCache() {
        return CodeCache.getInstance();
    }

    @Override
    public void init() {
        FileDataCache.init(new CacheLoader<String, FileData>() {
            @Override
            public FileData load(String id) throws Exception {
                MapperFactory factory = (MapperFactory) BeanContainer.getInstance().getBean(MapperFactory.class);
                BaseMapper mapper = factory.createMapper();
                EaFile useFile = mapper.getFileById(Integer.parseInt(id));
                File file = new File(fileRootPath +useFile.getId() +  useFile.getPath());
                FileInputStream reader = new FileInputStream(file);
                //TODO get fileData from file.
                return new FileData();
            }
        });
        CodeCache.init(new CacheLoader<String, String>() {
            @Override
            public String load(String s) throws Exception {
                return null;
            }
        });
        StringArrayCache.init(new CacheLoader<String, String[]>() {
            @Override
            public String[] load(String s) throws Exception {
                return new String[0];
            }
        });
    }

    @Override
    public void destroy() {

    }
}
