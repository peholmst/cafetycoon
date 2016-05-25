package org.vaadin.samples.cafetycoon.domain;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;

public class StaffMessageGenerator {
	
	public static final String[] LOREM_IPSUM = {
			"Aperiri placerat elaboraret ea eum, mea assum aliquid docendi ex.", 
			"Cetero corrumpit vim cu, eam ad elit etiam falli.",
			"Pri philosophia intellegebat eu.",
			"Dico dicunt argumentum nam at, has no autem ludus iuvaret.",
			"Per wisi iracundia temporibus ex.",
			"Labitur fuisset no cum.",
			"Sed cu placerat adolescens, eam vero meliore temporibus cu, duo commune expetenda eu.",
			"Et malis fabellas mei.",
			"Nec salutandi facilisis no, ei accusam mediocritatem ius.",
			"Eu aliquam dolores vim, tale dolorum repudiare pri an.",
			"Ei ignota accumsan vix, est adhuc accusamus evertitur cu.",
			"Malis nonumy nullam no ius, eu verterem constituto accommodare pri.",
			"Vix at movet accusam petentium.",
			"Habeo detraxit at eum, nisl veri debitis eum ei.",
			"Et alia quaerendum definitiones mel, epicuri dissentias ne sed.",
			"Volumus nostrum ut ius, sed magna tacimates definitiones ex.",
			"Ut per odio eius probo, audire assentior has te.",
			"Quo lorem meliore pertinacia te, vim minim everti an.",
			"Alii agam exerci et est, pro et alterum ornatus, quaeque nusquam senserit ne qui.",
			"No mei propriae voluptua.",
			"Feugiat deseruisse cu his.",
			"Ne soluta expetenda sea.",
			"Id molestie perfecto cum, te utamur offendit verterem his.",
			"Eu sea nullam molestie singulis, vel odio molestiae dissentiet cu, ludus nostro in ius."
	};
    private static final Logger LOGGER = LoggerFactory.getLogger(StaffMessageGenerator.class);
    private final Random rnd = new Random();	
	private final EmployeeRepository employeeRepository;
	private final StaffMessageService staffMessageService;
	private ScheduledExecutorService executorService;
		
	public StaffMessageGenerator(EmployeeRepository employeeRepository, StaffMessageService staffMessageService) {
		this.employeeRepository = employeeRepository;
		this.staffMessageService = staffMessageService;
	}

	public synchronized void start() {
		if (executorService == null) {
			executorService = Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleWithFixedDelay(this::generateMessage, 10, 10, TimeUnit.SECONDS);
		}
	}
	
	public synchronized void stop() {
		if (executorService != null) {
			try {
				executorService.shutdown();
			} finally {
				executorService = null;
			}
		}
	}
	
	public void generateMessage() {
		Employee employee = RandomUtils.pickRandom(employeeRepository.getEmployees());
		String message = LOREM_IPSUM[rnd.nextInt(LOREM_IPSUM.length)];
		LOGGER.info("Generating employee message from {}", employee);
		staffMessageService.sendMessage(employee, message);
	}
}
