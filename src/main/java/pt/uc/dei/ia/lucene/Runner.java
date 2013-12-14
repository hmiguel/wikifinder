package pt.uc.dei.ia.lucene;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Lucene luc = new Lucene();
		
	//	luc.buildIndex();
		
		System.out.println(luc.QuerySearch("london"));
		
		//luc.buildIndex();
		
	}

}
