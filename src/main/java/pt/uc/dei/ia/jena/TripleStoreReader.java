package pt.uc.dei.ia.jena;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pt.uc.dei.ia.models.Article;
import pt.uc.dei.ia.models.Category;
import pt.uc.dei.ia.models.IDs;
import pt.uc.dei.ia.models.NER;
import pt.uc.dei.ia.models.NER.Date;
import pt.uc.dei.ia.models.NER.Local;
import pt.uc.dei.ia.models.NER.Money;
import pt.uc.dei.ia.models.NER.Org;
import pt.uc.dei.ia.models.NER.Percent;
import pt.uc.dei.ia.models.NER.Person;
import pt.uc.dei.ia.models.NER.Time;
import pt.uc.dei.ia.models.Topic;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class TripleStoreReader {

	public static List<Article> getArticles() {

		OntDocumentManager mgr = new OntDocumentManager();
		OntModelSpec s = new OntModelSpec(OntModelSpec.RDFS_MEM);
		OntModel m = ModelFactory.createOntologyModel(s, null);
		s.setDocumentManager(mgr);

		InputStream in = FileManager.get().open("/media/Media/wikipedia/ontology/wikipedia.owl");

		// read the ontology file
		m.read(in, "");
		ExtendedIterator<Individual> individuals = m.listIndividuals();

		List<Article> articles = new ArrayList<Article>();

		NER ner = new NER();
		
		while (individuals.hasNext()) {
			Individual i = individuals.next();
			
			List<Category> categories = new ArrayList<Category>();
			
			List<Topic> topics = new ArrayList<Topic>(); // topics
			
			List<IDs> ids = new ArrayList<IDs>(); // ids

			List<NER.Person> persons = new ArrayList<NER.Person>(); // persones
			List<NER.Org> organizations = new ArrayList<NER.Org>(); // organizations
			List<NER.Date> dates = new ArrayList<NER.Date>(); // dates
			List<NER.Money> moneys = new ArrayList<NER.Money>(); // money
			List<NER.Time> times = new ArrayList<NER.Time>(); // times
			List<NER.Percent> percents = new ArrayList<NER.Percent>(); // percent
			List<NER.Local> locals = new ArrayList<NER.Local>(); // percent
			
			
			
			if (i.hasOntClass("file:///home/hmiguel/workspace/wikifinder/#Article") || i.hasOntClass("file:///home/hmiguel/workspace/wikifinder/#Topic")) {
				
				

				// NEW ARTICLE
				Article a = new Article();


				StmtIterator iter = i.listProperties();

				while (iter.hasNext()) {
					Statement stmt = iter.nextStatement(); // get next statement
					Resource subject = stmt.getSubject();
					Property predicate = stmt.getPredicate();
					RDFNode object = stmt.getObject(); // get the object
					if (object.isLiteral()) {

						// Set hasArticle
						if (predicate.getLocalName().equals("hasAbstract")) {
							a.setAbstract(object.asLiteral().getString());
						}

						// Set hasLabel
						else if (predicate.getLocalName().equals("hasTitle")) {
							a.setLabel(object.asLiteral().getString());
						}

						// Set hasResource
						else if (predicate.getLocalName().equals("hasResource")) {
							a.setResource(object.asLiteral().getString());
						}

						// Set hasCategory
						else if (predicate.getLocalName().equals("hasCategory")) {
							Category c = new Category();
							c.setCategory(object.asLiteral().getString());
							categories.add(c);
						}

						// TOPICS

						// Set hasName (Topic)
						else if (predicate.getLocalName().equals("hasName")) {
							Topic t = new Topic();
							t.setTopic(object.asLiteral().getString());
							topics.add(t);
						}
						
						// Set hasID
						else if (predicate.getLocalName().equals("hasID")) {
							IDs id = new IDs();
							id.setID(Integer.parseInt(object.asLiteral().getString()));
							ids.add(id);
						}

						// NER ELEMENTS

						// Set hasPeople
						else if (predicate.getLocalName().equals("hasPerson")) {
							Person p = ner.new Person();
							p.setPerson(object.asLiteral().getString());
							persons.add(p);
						}

						// Set hasLocal
						else if (predicate.getLocalName().equals("hasLocation")) {
							Local l = ner.new Local();
							l.setLocal(object.asLiteral().getString());
							locals.add(l);
						}

						// Set hasOrnanization
						else if (predicate.getLocalName().equals(
								"hasOrganization")) {
							Org o = ner.new Org();
							o.setOrganization(object.asLiteral().getString());
							organizations.add(o);
						}

						// Set hasDate
						else if (predicate.getLocalName().equals("hasDate")) {
							Date d = ner.new Date();
							d.setDate(object.asLiteral().getString());
							dates.add(d);
						}

						// Set hasTime
						else if (predicate.getLocalName().equals("hasTime")) {
							Time t = ner.new Time();
							t.setTime(object.asLiteral().getString());
							times.add(t);
						}

						// Set hasMoney
						else if (predicate.getLocalName().equals("hasMoney")) {
							Money money = ner.new Money();
							money.setMoney(object.asLiteral().getString());
							moneys.add(money);
						}

						// Set hasPercent
						else if (predicate.getLocalName().equals("hasPercent")) {
							Percent per = ner.new Percent();
							per.setPercent(object.asLiteral().getString());
							percents.add(per);
						}
						else{
							System.out.println("ERROR: " + subject.getURI().substring(38)
									 + "\t" + predicate.getLocalName() + "\t"
									 + object.asLiteral().getString());
							
						}

						// System.out.println(subject.getURI().substring(38)
						// + "\t" + predicate.getLocalName() + "\t"
						// + object.asLiteral().getString());
					}
				}

				// ADD LISTS TO ARTICLE OBJECT
				a.setPersons(persons);
				a.setOrganizations(organizations);
				a.setLocals(locals);
				a.setDates(dates);
				a.setTimes(times);
				a.setTopics(topics);
				a.setCategories(categories);
				a.setPercents(percents);
				a.setMoneyList(moneys);
				a.setIDs(ids);

				// ADD article to Articles List
				articles.add(a);
								
			}

		}
	

		return articles;

	}

}