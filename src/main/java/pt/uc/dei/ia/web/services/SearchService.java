package pt.uc.dei.ia.web.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import pt.uc.dei.ia.lucene.Lucene;

//import pt.uc.dei.ia.lucene.Lucene;


@Produces(MediaType.APPLICATION_JSON)
@Path("search/")
public class SearchService {

	@GET
	@Path("{query}")
	public Response getQueryResults(@PathParam("query") String query) {
		// TODO
		Lucene lu = new Lucene();

		try {
			JSONObject search = lu.QuerySearch(query);
		
			
			if (search.length() > 0) {

				return Response.ok(search).build();

			} else {

				return Response.ok(new JSONObject()).build();

			}
		} catch (Exception e) {
			return Response.ok(new JSONObject()).build();
		}
	}
	/*
	@GET
	@Path("sugestion/{word}")
	public Response getSugestion(@PathParam("word") String word) {
		
		JSONObject sugestions = new JSONObject();

		if (word.isEmpty()) {
			return Response.ok(new JSONObject()).build();
		}

		String[] bits = word.split(" ");
		String lastWord = bits[bits.length - 1];

		// Fetch suggestions
		// string[] suggestions = SearchSvc.SuggestTermsFor(lastWord).ToArray();

		// JSONArray
		JSONArray suglist = new JSONArray();
	//	suglist.
		
		try {
			sugestions.put("sugestion", suglist);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.ok(new JSONObject()).build();
	}
	*/

}
