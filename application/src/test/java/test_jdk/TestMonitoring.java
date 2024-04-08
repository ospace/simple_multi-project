package test_jdk;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestMonitoring {
    private static final Logger logger = LoggerFactory.getLogger(TestMonitoring.class);


    @Test
    public void testCpuLoad() throws InterruptedException {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        logger.info("os : version[{}] arch[{}] processors[{}]", osBean.getVersion(), osBean.getArch(), osBean.getAvailableProcessors());

        double load = 0.0;
        while (load == 0.0) {
            load = osBean.getSystemLoadAverage();
            Thread.sleep(1000);
            //loadCpu(1000);
        }

        logger.info("load[{} %]", load * 100.0);

        for (int i = 0; i < 10; ++i) {
            logger.info("[{}] load[{} %]", i, osBean.getSystemLoadAverage() * 100.0);
            Thread.sleep(1000);
        }

        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean osBean2 = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            osBean2.getProcessCpuLoad();
            Thread.sleep(1000);
            load = osBean2.getProcessCpuLoad();
			
			/*
			load=0.0;
			while(load==0.0) {
				load = osBean2.getProcessCpuLoad();
				Thread.sleep(1000);
				//loadCpu(1000);
			}
			*/

            logger.info("load2[{} %]", load * 100.0);
        }
    }

    @Test
    public void testMem() throws InterruptedException {
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();

        MemoryUsage heapMem = memBean.getHeapMemoryUsage();
        logger.info("heapMemory    : used[{} kB] max[{} kB] commited[{} kB] init[{} kB]", heapMem.getUsed() / 1024, heapMem.getMax() / 1024, heapMem.getCommitted() / 1024, heapMem.getInit() / 1024);
        MemoryUsage nonHeapMem = memBean.getNonHeapMemoryUsage();
        logger.info("nonHeapMemory : used[{} kB] max[{} kB] commited[{} kB] init[{} kB]", nonHeapMem.getUsed() / 1024, nonHeapMem.getMax() / 1024, nonHeapMem.getCommitted() / 1024, nonHeapMem.getInit() / 1024);
        logger.info("nonHeapMemory : used[{}] max[{}] commited[{}] init[{}]", nonHeapMem.getUsed(), nonHeapMem.getMax(), nonHeapMem.getCommitted(), nonHeapMem.getInit());

    }

    @Test
    public void testSysInfo() throws InterruptedException {
        RuntimeMXBean mxBean = ManagementFactory.getPlatformMXBean(RuntimeMXBean.class);

        logger.info("name[{}] spec[{}] vm[{}] version[{}]", mxBean.getName(), mxBean.getSpecName(), mxBean.getVmName(), mxBean.getVmVersion());

        List<OperatingSystemMXBean> list = ManagementFactory.getPlatformMXBeans(OperatingSystemMXBean.class);
        for (OperatingSystemMXBean it : list) {
            logger.info("name[{}] arch[{}]", it.getName(), it.getArch());
        }
    }

    @Test
    public void testThreadCpu() {
        //ThreadMXBean thdBean = ManagementFactory.getThreadMXBean();
        //thdBean.
    }

    //Ref: https://gist.github.com/lpsandaruwan/f2cef0aa91ae68cb041c7ecda04a0724
    @SuppressWarnings("restriction")
    @Test
    public void testJvmCpu() throws InterruptedException {
        com.sun.management.OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        //OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        int numProcessor = osBean.getAvailableProcessors();


        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();

        long upTime = 0, cpuTime = 0;

        cpuTime = osBean.getProcessCpuTime();
        upTime = runtimeBean.getUptime();

        for (int i = 0; i < 10; ++i) {
            //loadCpu(1000);
            Thread.sleep(1000);

            cpuTime = osBean.getProcessCpuTime() - cpuTime;
            upTime = runtimeBean.getUptime() - upTime;

            long totalUpTime = runtimeBean.getUptime() * numProcessor;

            float cpuUsage = cpuTime / (totalUpTime * 10000F);

            cpuTime = osBean.getProcessCpuTime();
            upTime = runtimeBean.getUptime();

            logger.info("[{}] jvm : cpu[{} %]", i, cpuUsage);

        }
    }

    static long loadCpu(long duration) {
        long runtime = System.currentTimeMillis();
        long sum = 0;
        while (duration > (System.currentTimeMillis() - runtime)) {
            ++sum;
        }
        return sum;
    }
}
