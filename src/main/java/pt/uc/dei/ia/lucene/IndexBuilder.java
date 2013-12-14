package pt.uc.dei.ia.lucene;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;


import pt.uc.dei.ia.models.Category;
import pt.uc.dei.ia.models.NER.Date;
import pt.uc.dei.ia.models.NER.Local;
import pt.uc.dei.ia.models.NER.Money;
import pt.uc.dei.ia.models.NER.Org;
import pt.uc.dei.ia.models.NER.Percent;
import pt.uc.dei.ia.models.NER.Person;
import pt.uc.dei.ia.models.NER.Time;

import pt.uc.dei.ia.models.Topic;

import pt.uc.dei.ia.models.Article;

public class IndexBuilder {

	public static void addDoc(IndexWriter w, Article a) throws IOException {
		Document doc = new Document();
	
		
		// LABEL
		doc.add(new TextField("label", a.getLabel(), Field.Store.YES)); // Label
		
		// ABSTRACT
		doc.add(new TextField("abstract", a.getAbstract(), Field.Store.YES)); // Abstract 
		
		// TOPICS
		List<Topic> topics = a.getTopics();
		for(Topic topic : topics){ 
			doc.add(new TextField("topic", topic.getTopic(), Field.Store.YES));	
		}
		
		// PERSONS
		List<Person> names = a.getPersons();
		for(Person name : names){
			doc.add(new TextField("person", name.getPerson(), Field.Store.YES));
		}
		
		// ORGANIZATION
		List<Org> orgs = a.getOrganizations();
		for(Org org : orgs){ 
			doc.add(new TextField("organization", org.getOrganization(), Field.Store.YES));	
		}
		
		// LOCAL
		List<Local> locals = a.getLocals();
		for(Local local : locals){ 
			doc.add(new TextField("local", local.getLocal() , Field.Store.YES));	
			
		}
		
		// DATE
		List<Date> dates = a.getDates();
		for(Date date : dates){ 
			doc.add(new TextField("date", date.getDate() , Field.Store.YES));	
		}
		
		// MONEY
		List<Money> moneyl = a.getMoneyList();
		for(Money money : moneyl){ 
			doc.add(new TextField("money", money.getMoney() , Field.Store.YES));	
		}
		
		// PERCENT
		List<Percent> perc = a.getPercents();
		for(Percent per : perc){ 
			doc.add(new TextField("date", per.getPercent() , Field.Store.YES));	
		}
		
		// TIME
		List<Time> times = a.getTimes();
		for(Time time : times){ 
			doc.add(new StringField("time", time.getTime() , Field.Store.YES));	
		}
		
		// CATEGORY
		List<Category> cats = a.getCategories();
		for(Category cat : cats){ 
				doc.add(new TextField("category", cat.getCategory() , Field.Store.YES));	
		}
		
		//RESOURCE
		System.out.println(a.getResource());
		doc.add(new TextField("resource", a.getResource(), Field.Store.YES)); // Abstract 

		// SAVE DOC
		w.addDocument(doc);
	}

}
