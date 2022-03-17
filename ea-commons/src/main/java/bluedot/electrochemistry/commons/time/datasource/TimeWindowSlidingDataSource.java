package bluedot.electrochemistry.commons.time.datasource;

import bluedot.electrochemistry.commons.time.exception.TimeWindowSlidingDataSourceException;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Senn
 * @create 2022/3/7 10:56
 */
public interface TimeWindowSlidingDataSource {
    /**
     * 记录通过记录
     *
     * @param timeSlices 时间分片
     * @param recordKey  记录参数
     * @throws TimeWindowSlidingDataSourceException 时间分片数据源操作异常
     */
    void allocAdoptRecord(int timeSlices, String recordKey) throws TimeWindowSlidingDataSourceException;

    /**
     * 获取通过次数
     *
     * @param timeSlices 时间分片
     * @param recordKey  记录参数
     * @return int
     * @throws TimeWindowSlidingDataSourceException TimeWindowSlidingDataSourceException
     */
    int getAllocAdoptRecordTimes(int timeSlices, String recordKey) throws TimeWindowSlidingDataSourceException;

    /**
     * 将fromIndex~toIndex之间的时间片计数清零
     *
     * @param fromIndex   起始索引
     * @param toIndex     结束索引
     * @param totalLength 窗口数量
     * @throws TimeWindowSlidingDataSourceException 时间分片数据源操作异常
     */
    void clearBetween(int fromIndex, int toIndex, int totalLength) throws TimeWindowSlidingDataSourceException;


    static TimeWindowSlidingDataSource defaultDataSource() {
        return new TimeWindowSlidingDataSource() {

            private Map<String, Map<String, Integer>> timeWindowSlidingMap = new ConcurrentHashMap<>(16);

            @Override
            public void allocAdoptRecord(int timeSlices, String recordKey) throws TimeWindowSlidingDataSourceException {
                Map<String, Integer> timeSlicesMap = timeWindowSlidingMap.get(String.valueOf(timeSlices));
                if (Objects.isNull(timeSlicesMap)) {
                    timeSlicesMap = new ConcurrentHashMap<>(16);
                    timeWindowSlidingMap.put(String.valueOf(timeSlices), timeSlicesMap);
                }

                Integer adoptTimes = timeSlicesMap.get(recordKey);
                int nextTimes = adoptTimes == null ? 0 : adoptTimes;
                timeSlicesMap.put(recordKey, ++nextTimes);
            }

            @Override
            public int getAllocAdoptRecordTimes(int timeSlices, String recordKey) throws TimeWindowSlidingDataSourceException {
                Map<String, Integer> timeSlicesMap = timeWindowSlidingMap.get(String.valueOf(timeSlices));
                if (Objects.isNull(timeSlicesMap)) {
                    return 0;
                }
                Integer recordTimes = timeSlicesMap.get(recordKey);
                return Objects.isNull(recordTimes) ? 0 : recordTimes;
            }

            @Override
            public void clearBetween(int fromIndex, int toIndex, int totalLength) throws TimeWindowSlidingDataSourceException {
                if (fromIndex >= toIndex) {
                    toIndex += totalLength;
                }
                while (fromIndex <= toIndex) {
                    Map<String, Integer> timeWindowSlidingScopeMap = timeWindowSlidingMap.get(String.valueOf(fromIndex % totalLength));
                    if (!Objects.isNull(timeWindowSlidingScopeMap) && timeWindowSlidingScopeMap.size() > 0) {
                        timeWindowSlidingScopeMap.clear();
                    }
                    fromIndex++;
                }
            }

        };
    }
}