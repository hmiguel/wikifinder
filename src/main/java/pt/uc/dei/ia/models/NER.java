package pt.uc.dei.ia.models;

public class NER {
	
	public class Person{
		
		private String person;
		
		public void setPerson(String person) {
			this.person = person;
		}

		public String getPerson() {
			return person;
		}
		
	}
	
	public class Org{
		
		private String org;
		
		public void setOrganization(String org) {
			this.org = org;
		}

		public String getOrganization() {
			return org;
		}
		
	}

	public class Date{
		
		private String date;
		
		public void setDate(String date) {
			this.date = date;
		}

		public String getDate() {
			return date;
		}
		
	}
	
	public class Local{
		
		private String local;
		
		public void setLocal(String local) {
			this.local = local;
		}

		public String getLocal() {
			return local;
		}
		
	}

	public class Money{
	
		private String money;
		
		public void setMoney(String money) {
			this.money = money;
		}

		public String getMoney() {
			return money;
		}
	
	}
	
	public class Percent{
		
		private String percent;
		
		public void setPercent(String percent) {
			this.percent = percent;
		}

		public String getPercent() {
			return percent;
		}
		
	}
	
	public class Time{
		private String time;
		
		public void setTime(String time) {
			this.time = time;
		}

		public String getTime() {
			return time;
		}
		
		
	}

}
