package hw4;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Locale.Category;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.bson.*;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static java.util.Arrays.asList;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Aggregates.*;
import static java.util.Arrays.asList;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Aggregates.lookup;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Filters.eq;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import java.awt.Font;
import java.awt.Window.Type;
import java.util.Locale;

public class Hw4 extends JFrame {
    private static MongoClient client;
    private JPanel MainPane;
    private JTable businessTable;
    private String selected_catg;
    private DefaultTableModel tableList;
    List<String> BusinessSet;
    JScrollPane tableScrollPane;
    JComboBox proximityComboBox;
    JComboBox searchForComboBox;
    String b_id;
    JTable reviewTable ;
    JScrollPane scrollPane_review;
	private CatPanelCheckBoxActionHandler catPanelCheckBoxActionHandler;
	private CatPanelCheckBoxActionHandler SubcatPanelCheckBoxActionHandler;
	private CatPanelCheckBoxActionHandler AttributePanelCheckBoxActionHandler;
	JComboBox pointOfInterestComboBox = new JComboBox();
    JPanel mainCatPanel = new JPanel();
    JPanel attributePanel = new JPanel();
    ArrayList<String> addresses = new ArrayList<String>();

    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Hw4 frame = new Hw4();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     * @param pointOfInterestComboBox 
     * @throws Exception
     */
    public Hw4() throws Exception {
    	setBackground(new Color(0, 153, 51));
    	setFont(new Font("Arial Black", Font.PLAIN, 12));
        setName("mainFrame");
        setTitle("DBA Yelp MongoDB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        MainPane = new JPanel();
        MainPane.setBackground(new Color(147, 112, 219));
        MainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(MainPane);
        
       
        JScrollPane mainCatScrollPane = new JScrollPane();
       
        JScrollPane categoryScrollPane = new JScrollPane();
       
        
     
       
        JLabel lblPointOfInterest = new JLabel("Point of Interest");
       
        proximityComboBox = new JComboBox();
        proximityComboBox.setModel(new DefaultComboBoxModel(new String[] {"NONE", "5", "10", "20", "30", "50"}));
       
        JLabel lblProximity = new JLabel("Proximity");
       
        searchForComboBox = new JComboBox();
        searchForComboBox.setModel(new DefaultComboBoxModel(new String[] {"OR", "AND"}));
       
        JLabel lblSearchFor = new JLabel("Search For");

        JButton btnSearch = new JButton("Search");
        tableScrollPane = new JScrollPane();
        tableScrollPane.setLocale(Locale.US);
        
        businessTable = new JTable();

		tableScrollPane.setViewportView(businessTable);
       
        businessTable.setShowGrid(true);
        
        AddressClass address1 = new AddressClass("4237 Lien Rd\nSte H\nMayfair Park\nMadison, WI 53704", -89.3083801269531, 43.1205749511719);
		AddressClass address2 = new AddressClass("4840 E Indian School Rd\nSte 101\nPhoenix, AZ 85018",  -111.983757019043, 33.4993133544922);
		AddressClass address3 = new AddressClass("Mesa, AZ 85206", -111.701843261719, 33.3951606750488);
		AddressClass address4 = new AddressClass("3921 E Baseline Rd\nSte 108\nGilbert, AZ 85234",  -111.747520446777, 33.3782119750977);
		AddressClass address5 = new AddressClass("1610 Lake Las Vegas Pkwy\nHenderson, NV 89011", -114.932034, 36.102125000000001);
		pointOfInterestComboBox.setModel(new DefaultComboBoxModel(new String[] {"NONE", address1.Address,address2.Address,address3.Address ,address4.Address ,address5.Address}));
         
		
        btnSearch.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		tableList = new DefaultTableModel(new String[]{"Name", "City","State","Stars"},0);
        		 businessTable.setModel(tableList);
        		 businessTable.revalidate();
                 businessTable.repaint();
                 if(pointOfInterestComboBox.getSelectedItem().toString()=="NONE" || proximityComboBox.getSelectedItem().toString() == "NONE")
                 {
                	 BusinessSet = PopulateBusinessOnSearch();
                 }
                 else
                 {
                	 BusinessSet = PopulateBusinessesProximityOnSearch();
                 }
        		
        		try {
	        		for(String str:BusinessSet){
	        			JSONParser parser = new JSONParser();
	                    JSONObject jsonObject = (JSONObject) parser.parse(str);
	                    String Name = (String) jsonObject.get("name");
	                    String city = (String) jsonObject.get("city");
	                    String state = (String) jsonObject.get("state");
	                    String stars = (String) jsonObject.get("stars");
	                    b_id=(String)jsonObject.get("business_id");
	                    //populateReview(b_id);
	                    tableList.addRow(new Object[]{Name,city,state,stars});
	                    businessTable.revalidate();
	                    businessTable.repaint();
	                    tableScrollPane.revalidate();
	                    tableScrollPane.repaint();
	        		}
        		} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
     	}
      });
        
        businessTable.revalidate();
        businessTable.repaint();
        tableScrollPane.revalidate();
        tableScrollPane.repaint();
        businessTable.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				int row = businessTable.rowAtPoint(e.getPoint());
				String business_name = businessTable.getModel().getValueAt(row, 0).toString();
				if (row>=0)
				{
					populateReview(business_name);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
      
        
        JCheckBox checkbox;
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                client.getDatabase("YelpApplication").getCollection("YelpBusinessAttributes").drop();
            }
        });
        
        JButton btnSelectCategory = new JButton("Select Category");
        btnSelectCategory.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		ArrayList<String> categoryList = catPanelCheckBoxActionHandler.getCategoriesSelected();
        		ArrayList<String> attributeList = callAttributes(categoryList);
        		PopulateAttributes(attributeList);
        	}
        });
        
               
        GroupLayout gl_MainPane = new GroupLayout(MainPane);
        gl_MainPane.setHorizontalGroup(
        	gl_MainPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_MainPane.createSequentialGroup()
        			.addGroup(gl_MainPane.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(mainCatScrollPane, GroupLayout.PREFERRED_SIZE, 192, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addGap(42)
        					.addComponent(btnSelectCategory)))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_MainPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addComponent(categoryScrollPane, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(tableScrollPane, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
        					.addContainerGap())
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addGroup(gl_MainPane.createParallelGroup(Alignment.LEADING)
        						.addComponent(pointOfInterestComboBox, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
        						.addComponent(lblPointOfInterest))
        					.addGap(6)
        					.addGroup(gl_MainPane.createParallelGroup(Alignment.LEADING)
        						.addComponent(proximityComboBox, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
        						.addComponent(lblProximity))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_MainPane.createParallelGroup(Alignment.LEADING)
        						.addComponent(lblSearchFor)
        						.addComponent(searchForComboBox, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
        					.addGap(18)
        					.addComponent(btnSearch)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnClose)
        					.addGap(44))))
        );
        gl_MainPane.setVerticalGroup(
        	gl_MainPane.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_MainPane.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_MainPane.createParallelGroup(Alignment.TRAILING)
        				.addComponent(tableScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        				.addComponent(categoryScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        				.addComponent(mainCatScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
        			.addGap(21)
        			.addGroup(gl_MainPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addGroup(gl_MainPane.createParallelGroup(Alignment.BASELINE)
        						.addComponent(btnSearch)
        						.addComponent(btnClose))
        					.addContainerGap())
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addGroup(gl_MainPane.createParallelGroup(Alignment.BASELINE)
        						.addComponent(lblSearchFor)
        						.addComponent(lblPointOfInterest)
        						.addComponent(lblProximity))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(gl_MainPane.createParallelGroup(Alignment.BASELINE)
        						.addComponent(pointOfInterestComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(proximityComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        						.addComponent(searchForComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        				.addGroup(gl_MainPane.createSequentialGroup()
        					.addComponent(btnSelectCategory)
        					.addContainerGap())))
        );
       
        String[] categories={"Active Life","Arts & Entertainment","Automotive","Car Rental","Cafes","Beauty & Spas","Convenience Stores",
                "Dentists", "Doctors", "Drugstores", "Department Stores","Education", "Event Planning & Services", "Flowers & Gifts",
                "Food", "Health & Medical", "Home Services","Home & Garden","Hospitals","Hotels & Travel","Hardware Stores","Grocery",
                "Medical Centers", "Nurseries & Gardening","Nightlife","Restaurants","Shopping","Transportation"};
        mainCatPanel.setBackground(new Color(230, 230, 250));

        mainCatPanel.setLayout(new GridLayout(categories.length, 1, 0, 0));
        this.catPanelCheckBoxActionHandler = new CatPanelCheckBoxActionHandler();
        for(String category:categories){
            checkbox=new JCheckBox(category);
            checkbox.addActionListener(catPanelCheckBoxActionHandler);
            System.out.println("categories are :"+category);            
            mainCatPanel.add(checkbox);
        }
        attributePanel.setAutoscrolls(true);
        attributePanel.setBackground(new Color(230, 230, 250));
        attributePanel.setForeground(new Color(100, 149, 237));
        attributePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
       
        
       
       
        categoryScrollPane.setViewportView(attributePanel);
       
       
        mainCatScrollPane.setViewportView(mainCatPanel);
        MainPane.setLayout(gl_MainPane);
        connection();
    }
    
    protected List<String> PopulateBusinessesProximityOnSearch(){
    	DB db1 = new MongoClient("localhost",27017).getDB("YelpApplication");
    	DBCollection dbcollection=db1.getCollection("YelpBusinessAttributes");
    	BasicDBObject q = new BasicDBObject();
    	BasicDBObject proj1 = new BasicDBObject();
    	
		ArrayList<String> str = catPanelCheckBoxActionHandler.getCategoriesSelected();
		
		ArrayList<String> str2 = AttributePanelCheckBoxActionHandler.getCategoriesSelected();
		String option;
		if(searchForComboBox.getSelectedItem().toString() == "AND"){
			option = "$all";
		}else{
			option = "$in";
		}
    	q.put("categories", new BasicDBObject(option, str));
    	q.append("attribute_keys", new BasicDBObject(option, str2));
    	proj1.put("name", 1);
    	proj1.append("city", 1);
    	proj1.append("stars", 1);
    	proj1.append("state", 1);
    	proj1.append("_id", 0);
   	
    	String pointOfInterested_Value = pointOfInterestComboBox.getSelectedItem().toString();
        	
    	BasicDBObject full = new BasicDBObject();
        full.append("full_address", pointOfInterested_Value);
        BasicDBObject proj=new BasicDBObject();
        proj.put("Loc", 1);
        DBCursor cursor = dbcollection.find(full,proj);
        DBObject res;
        double latitude = 0;
        double longitude = 0;
        while(cursor.hasNext()){
        	res=cursor.next();
        	System.out.println("cursor is:"+res);
        	DBObject report=(DBObject) res.get("Loc");
        	latitude=Double.parseDouble(report.get("latitude").toString().trim());
        	longitude=Double.parseDouble(report.get("longitude").toString().trim());
        	System.out.println("latitude" + latitude);
        	System.out.println("longitude" + longitude);
        }

        	BasicDBList geoCoord = new BasicDBList();
            geoCoord.add(longitude);
            geoCoord.add(latitude);

            BasicDBList geoParams = new BasicDBList();
            geoParams.add(geoCoord);
            System.out.println(proximityComboBox.getSelectedItem());
            geoParams.add( Float.parseFloat(proximityComboBox.getSelectedItem().toString())/3963.2);


            q.append("Loc", new BasicDBObject("$geoWithin", new BasicDBObject("$centerSphere", geoParams)));
            System.out.println("FromHere");
            DBCursor sex = dbcollection.find(q,proj1);
            List<String> businessList = new ArrayList<String>();

    	       while(sex.hasNext()){
    	    	 String res1 = sex.next().toString();
    	       	System.out.println(res1);
    	       	businessList.add(res1);
    	       }
    	       return businessList;
    }
         
    
    
    protected List<String> PopulateBusinessOnSearch() {
		// TODO Auto-generated method stub
    	MongoCollection<Document> b_collection=client.getDatabase("YelpApplication").getCollection("YelpBusinessAttributes");
    	ArrayList<String> str = catPanelCheckBoxActionHandler.getCategoriesSelected();
    	ArrayList<String> str2 = AttributePanelCheckBoxActionHandler.getCategoriesSelected(); 
    	
    	for(String str3: str){
    		System.out.println(str3);
    	}
    	
    	for(String str3: str2){
    		System.out.println(str3);
    	}
    	String option;
		if(searchForComboBox.getSelectedItem().toString() == "AND"){
			option = "$all";
		}else{
			option = "$in";
		}
    	FindIterable<Document> result = b_collection.find(new Document("categories",new Document(option, str)).append("attribute_keys", new Document(option, str2))).projection(fields(include("name", "city", "stars", "state"), excludeId()));
         List<String> businesses = new ArrayList<String>();
         result.forEach(new Block<Document> (){
         	@Override
 			public void apply(final Document document) {
 				// TODO Auto-generated method stub
         		businesses.add(document.toJson().toString().trim());
         		System.out.println(document.toJson().toString().trim());
 			}
         	});
         return businesses;
	}

	protected void PopulateAttributes(ArrayList<String> attributeList) {
		// TODO Auto-generated method stub
    	attributePanel.removeAll();
        attributePanel.setLayout(new GridLayout(attributeList.size(), 1,0,0));
        AttributePanelCheckBoxActionHandler = new CatPanelCheckBoxActionHandler();
        JCheckBox checkbox1;
        for(String attribute:attributeList){
        	checkbox1 =new JCheckBox(attribute);
            checkbox1.addActionListener(AttributePanelCheckBoxActionHandler);
            System.out.println("attribute are :"+attribute);            
            attributePanel.add(checkbox1);
        }
        attributePanel.revalidate();
        attributePanel.repaint();
	}

	private void connection() throws Exception{
        client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        if (client != null)
        {
            System.out.println("Connection Established");
            MongoCollection<Document> b_collection = client.getDatabase("YelpApplication").getCollection("YelpBusiness");
            MongoCollection<Document> new_collection = client.getDatabase("YelpApplication").getCollection("YelpBusinessAttributes");
	          FindIterable<Document> cursor = b_collection.find();
	          List<Document> tempbusiness = new ArrayList<Document>();
	          cursor.forEach(new Block<Document>(){

				@Override
				public void apply(final Document document) {
					// TODO Auto-generated method stub
					try {
			       		  	  //tempbusinessid.add(cursor);
			                  JSONParser parser = new JSONParser();
			                  JSONObject jsonObject;
			  
			                  jsonObject = (JSONObject)parser.parse(document.toJson());
			                  				  
			                  String business_id = (String) jsonObject.get("business_id");
			                  Document doc = new Document("b_id", business_id);
			                  String full_address = (String)jsonObject.get("full_address");
			                  doc.append("full_address", full_address);
			                  String city = (String) jsonObject.get("city");
			                  doc.append("city", city);
			                  String review_count = jsonObject.get("review_count").toString();
			                  doc.append("review_count", Integer.parseInt(review_count));
			                  String businessName = jsonObject.get("name").toString();
			                  doc.append("name", businessName);
			                  String longitude = jsonObject.get("longitude").toString();
			                  String latitude = jsonObject.get("latitude").toString();
			                  
			                  Float [] Loc = {Float.parseFloat(longitude), Float.parseFloat(latitude)};
			                  doc.append("Loc", new Document().append("longitude", Float.parseFloat(longitude)).append("latitude", Float.parseFloat(latitude)));
//			                  JSONArray loc1 = new JSONArray(Arrays.asList(Loc));
			                  doc.append("longitude",longitude);
			                  doc.append("latitude", latitude);
			                  String state = (String) jsonObject.get("state");
			                  doc.append("state", state);
			                  String stars = jsonObject.get("stars").toString();
			                  doc.append("stars", stars);
			                  String bType = (String) jsonObject.get("type");
			                  doc.append("type",bType);
			                  JSONArray category=(JSONArray)jsonObject.get("categories");
			                  doc.append("categories", category);
			                  JSONObject attributes1 = (JSONObject) jsonObject.get("attributes");
			                  Object[] keys = attributes1.keySet().toArray();
			                  Object[] list = attributes1.values().toArray();
			                  ArrayList<String> valueList = new ArrayList<String>();
			                  BasicDBObject attr = new BasicDBObject();
			                  ArrayList<String> keys3=new ArrayList<String>();
			                  valueList.add("yes");
			                  valueList.add("no");
			                  valueList.add("true");
			                  valueList.add("false");
			                  for(int i =0; i != keys.length; i++){
			                      String value = list[i].toString();
			                      if(isJsonVerification(value)){
			                          JSONObject obj = (JSONObject) new JSONParser().parse(value);
			                          Object[] keys2 = obj.keySet().toArray();
			                          Object[] list2 = obj.values().toArray();
			                          for (int j = 0; j < keys2.length; j++) {
			                          	String value2 = list2[j].toString();
			                          //	System.out.println(keys2[j].toString() + " " + value2);
			                          	
			                          	if(valueList.contains(value2) ? true : false){
			                          		attr.append(keys[i].toString()+"_"+keys2[j].toString(), list2[j].toString());
			                          		keys3.add(keys[i].toString()+"_"+keys2[j].toString());	
			                          		} else{
			                                attr.append(keys[i].toString()+"_"+keys2[j].toString()+"_"+value2,  "true");
			                                keys3.add(keys[i].toString()+"_"+keys2[j].toString()+"_"+value2);
			                          	}
			                          }
			                      }
			                      else{
			                      	System.out.println(keys[i].toString() + " " + value);
			                      	if(valueList.contains(value) ? true : false){
			                      		attr.append(keys[i].toString(), list[i].toString());
			                      		keys3.add(keys[i].toString());
			                      	}else{
			                           attr.append(keys[i].toString()+"_"+list[i].toString(), "true");
			                           keys3.add(keys[i].toString()+"_"+list[i].toString());
			                      	}
			                      }
			                  }
			                  doc.append("attribute",attr );
			                  doc.append("attribute_keys",keys3);
			                  tempbusiness.add(doc);
//			   			  documentMap.add(doc);
			       	  }
			       	  catch( ParseException e){
			       		  e.printStackTrace();
			       		  
			       	  }
				}
	          });
	          new_collection.insertMany(tempbusiness);

        }
        else
        {
            System.out.println("Connection not establishd");
        }
    }
    public static boolean isJsonVerification(String str){
	       
	       if (str.trim().contains("{") || str.trim().contains("[")) {
	           return str.trim().charAt(0) == '{' || str.trim().charAt(0) == '[';
	       } else {
	           return false;
	       }
    }
    public ArrayList<String> callAttributes(ArrayList<String>  categorySelectQuery){     
        //Attributes Data
    	String option;
		if(searchForComboBox.getSelectedItem().toString() == "AND"){
			option = "$all";
		}else{
			option = "$in";
		}
        MongoCollection<Document> b_collection=client.getDatabase("YelpApplication").getCollection("YelpBusinessAttributes");
        FindIterable<Document> result = b_collection.find(new Document("categories",new Document(option, categorySelectQuery))).projection(fields(include("attribute"), excludeId()));
		List<String> attributes = new ArrayList<String>();
        result.forEach(new Block<Document> (){
        	@Override
			public void apply(final Document document) {
				// TODO Auto-generated method stub
        		JSONObject object;
				try {
					object = (JSONObject) new JSONParser().parse(document.toJson().toString());
					Object[] values = object.values().toArray();
					for(int i = 0; i!=values.length;i++) {
						if(isJsonVerification(values[i].toString())){
							Object[] keys = (((JSONObject)new JSONParser().parse(values[i].toString())).keySet().toArray());
							for(int j = 0; j!=keys.length;j++) {
								attributes.add(keys[i].toString());
//								System.out.println(keys[i].toString());
							}
						}							
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
			}
        	});
        ArrayList<String> attributeList = new ArrayList<String>(new LinkedHashSet<String>(attributes));
        for(String str:attributeList){
        	System.out.println(str);
        }
        return attributeList;
    }

    public void populateReview(String businessName){
    	JFrame reviewframe = new JFrame();
		reviewframe.setSize(600, 600);
		reviewframe.setTitle("Reviews Table");
		reviewframe.setVisible(true);
		reviewTable = new JTable();
		String[] columnNames = new String[]{"Review Date", "Stars", "Review Text","User Name","Useful Votes"};
		DefaultTableModel model1 = new DefaultTableModel(columnNames, 0);
		reviewTable.setModel(model1);
		reviewTable.setShowGrid(true);
    	MongoCollection<Document> b_collection=client.getDatabase("YelpApplication").getCollection("YelpBusiness");
    	AggregateIterable<Document> list = b_collection.aggregate(asList(match(eq("name", businessName)), 
    																lookup("YelpReview", "business_id", "business_id", "YelpBusinessReviews"), 
    																unwind("$YelpBusinessReviews"),
    																lookup("YelpUser", "YelpBusinessReviews.user_id", "user_id", "UserDetails"),
    																project(fields(computed("date", "$YelpBusinessReviews.date"), computed("stars", "$YelpBusinessReviews.stars"), computed("text", "$YelpBusinessReviews.text"), computed("userName", "$UserDetails.name"), computed("usefulVotes", "$YelpBusinessReviews.votes.useful"),  excludeId())),
    																unwind("$userName")));
    	try {
			for(Document str:list){
				System.out.println("list is: "+str.toJson().toString().trim());
   			   	JSONParser parser = new JSONParser();
   			   	JSONObject jsonObject = (JSONObject) parser.parse(str.toJson().toString().trim());
   			   	String date = (String) jsonObject.get("date");
   			   	String stars = (String) jsonObject.get("stars").toString();
   			   	String text = (String) jsonObject.get("text");
   			   	String user_name = (String) jsonObject.get("userName");
   			   	String votesUseful=(String) jsonObject.get("usefulVotes").toString();
   			   	model1.addRow(new Object[]{date, stars, text,user_name,votesUseful});
			}
		}
   		catch(Exception e){
   			System.out.println("Cant execute " + e);
   			}
    	
    	scrollPane_review = new JScrollPane();
		reviewframe.getContentPane().add(scrollPane_review);
		scrollPane_review.add(reviewTable);
		scrollPane_review.setViewportView(reviewTable);
		scrollPane_review.revalidate();
		scrollPane_review.repaint();
   		
}



class CatPanelCheckBoxActionHandler implements ActionListener {	
	 ArrayList<String> categoriesSelected = null;
	
	public CatPanelCheckBoxActionHandler() {
		this.categoriesSelected = new ArrayList<String>();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		JCheckBox cb = (JCheckBox) event.getSource();
       if (cb.isSelected()) {
           System.out.println("Checkbox "+cb.getText()+" is checked");
           //this.getCategoriesSelected().add(cb.getText());
           categoriesSelected.add(cb.getText());
           System.out.println(categoriesSelected.size());
       } else {
       	System.out.println("Checkbox "+cb.getText()+" is unchecked");
       	this.getCategoriesSelected().remove(cb.getText());
       }
	}

	/**
	 * @return the categoriesSelected
	 */
	public ArrayList<String> getCategoriesSelected() {
		return this.categoriesSelected;
	}

	/**
	 * @param categoriesSelected the categoriesSelected to set
	 */
	public void setCategoriesSelected(ArrayList<String> categoriesSelected) {
		this.categoriesSelected = categoriesSelected;
	}
	
	public void pane2Query(){
		System.out.println(categoriesSelected);
	}

}

public class AddressClass{
	public String Address;
	public double Lonlongitude;
	public double Latitude;
	public AddressClass(String Address, double d, double e){
		this.Address = Address;
		this.Lonlongitude = d;
		this.Latitude = e;
	}
}
}