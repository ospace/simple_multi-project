package util;


import java.util.List;

//import static org.hamcrest.CoreMatchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.stereotype.Component;

@Component
public class JUtils {
	public static float avg(List<Long> data) {
		data.sort((l,r)->l.compareTo(r));
		int cnt = data.size()-1;
		if (0 == cnt) return data.get(0);
		
		long sum = 0;
		for(int i=0; i<cnt; ++i) {
			sum += data.get(i);
		}
		return sum / cnt;
	}
}
