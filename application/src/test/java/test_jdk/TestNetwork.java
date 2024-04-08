package test_jdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestNetwork {
    private static final Logger logger = LoggerFactory.getLogger(TestNetwork.class);

    @Test
    public void testDns() throws IOException {
        String urlName0 = "ace.airasia.com";
        //String urlName0 = "47.88.146.98";

        long runtime = System.currentTimeMillis();
        logger.info("ip : {} >> {} ({} msec)", urlName0, InetAddress.getByName(urlName0).getHostAddress(), (System.currentTimeMillis() - runtime));


        String urlName = "https://ace.airasia.com/";

        runtime = System.currentTimeMillis();
        URL url = new URL(urlName);

        logger.info("host : {}", url.getHost());

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String ipaddr = br.readLine().trim();

        logger.info("ip : {} >> {} ({} msec)", urlName, ipaddr, (System.currentTimeMillis() - runtime));


    }

    @Test
    public void testUrl() throws UnknownHostException {
        String regex = "^(https?|ftp|file)://([-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)[-a-zA-Z0-9+&@#/%=~_|]";

        Pattern pattern = Pattern.compile(regex);
        String urlName = "https://ace.airasia.com/";
        Matcher matcher = pattern.matcher(urlName);

        while (matcher.find()) {
            String group = matcher.group();

            logger.info("matcher : group[{}] groupCount[{}]", group, matcher.groupCount());

            for (int i = 1; i <= matcher.groupCount(); ++i) {
                logger.info("[{}] {}", i, matcher.group(i));
            }

            if (2 == matcher.groupCount()) {
                String hostName = matcher.group(2);
                String ipAddr = InetAddress.getByName(hostName).getHostAddress();

                String res = urlName.replace(hostName, ipAddr);

                logger.info("{} : {} >> {} - {}", urlName, hostName, ipAddr, res);
            }
        }
    }
}
