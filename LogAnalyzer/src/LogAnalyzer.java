/**
 * HW4 - Log Analyzer
 * @author tgmerge
 */
public class LogAnalyzer {

	public static void main(String[] args) {
		
		// 打开LOG文件并处理
		System.out.println("=====Parsing...=====");		
		Log log = new Log("access.log");
		
		System.out.println("\n=====Parse done=====");
		System.out.println(log.dataCount() + " records.");
		
		Stat s;
		
		// 统计结果测试
		// which are the most popular pages they provide
		System.out.println("\n=====Target visited top 10=====");
		s = log.statTarget(10);
		s.print();
		System.out.println();
		
		// which ip took the most pages from the site
		System.out.println("\n=====Source IP top 10=====");	
		s = log.statIP(10);
		s.print();
		
		// whether other sites appear to have broken links to this site's pages
		System.out.println("\n=====Broken links visited top 10=====");
		s = log.statBroken(10);
		s.print();
		
		// how much data is being delivered to clients
		System.out.println("\n=====Transferred data size=====");
		s = log.statSize();
		s.print();
		
		// the busiest periods over the course of a day, or week, or month
		System.out.println("\n=====Stat visit by hour/day=====");
		try {
			s = log.statBusy("day");
			s.print();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\n=====Stat visit by day/month=====");
		try {
			s = log.statBusy("month");
			s.print();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("\n=====Stat visit by day/week=====");
		try {
			s = log.statBusy("week");
			s.print();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
