import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WordCounter extends Application {

	public static void main(String[] args) {
		Application.launch(args);

	}

	// -------------------------------Variables-----------------------------------------------------------------

	String sPath = " ";	//path to file
	String listPath = " ";		//data from copy paste
	String mainString = ""; //string for data
	String search = " ";	 	// search word 
	StringBuilder builderString = new StringBuilder();	//to get info from file and turn to string 
	String fileString = " ";	// string format from file 

	// -------------------------------------------------------------------------------------------
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//Main pane 
		BorderPane pane = new BorderPane();	

		// VBOX for search and  search results
		VBox vBox = new VBox();

		// Text field for search
		TextField searchTextField = new TextField();

		// Text field for showing results for search
		TextField searchResultsTextField = new TextField();
		searchResultsTextField.setDisable(true);
		searchTextField.setPromptText("Search");
		
		//vBox spacing 
		vBox.setPadding(new Insets(15, 15, 15, 15));
		vBox.setSpacing(15);
		
		// adding label (Search) and text fields to vBox
		vBox.getChildren().addAll(searchTextField, searchResultsTextField);

		// VBox for center of main border pane
		VBox vBoxCenter = new VBox();

		// hBox to hold  toggle  buttons
		HBox hBoxforlbl = new HBox();

		// TextField for entering data
		TextField textFieldForData = new TextField();
		textFieldForData.autosize();
		textFieldForData.setPromptText("Enter Data");

		//button for file name 
		RadioButton btnFileName = new RadioButton("File name");
		btnFileName.setPadding(new Insets(0, 15, 3, 15));

		//button for copy paste
		RadioButton btnCopyPaste = new RadioButton("Copy Paste");
		btnCopyPaste.setPadding(new Insets(0, 15, 3, 15));

		// search field set on enter
		searchTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					//getting search word 
					search = searchTextField.getText();

					// finding what string to search
					WordDisplays w;
					if (btnCopyPaste.isSelected()) { // String is from copy and paste
						w = new WordDisplays(mainString.toLowerCase(), search.toLowerCase());
						btnCopyPaste.setSelected(true);
					} 
					else {					 // String  from file
						w = new WordDisplays(fileString.toLowerCase(), search.toLowerCase());
					}
					searchResultsTextField.setText(w.toString());

					// clearing search text field
					searchTextField.clear();

				}
			}
		});

		// grouping the toggle buttons
		ToggleGroup group = new ToggleGroup();
		btnFileName.setToggleGroup(group);
		btnCopyPaste.setToggleGroup(group);

		// HBox for label (Enter data here and toggle buttons into hBox
		hBoxforlbl.getChildren().addAll(btnFileName, btnCopyPaste);

		// VBox for textAea that holds 5 most common words and a scroll pane for
		// alphabetical list and in natural order list
		vBoxCenter.setPadding(new Insets(15, 15, 15, 15));
		vBoxCenter.getChildren().addAll(hBoxforlbl, textFieldForData);

		// Main display for output
		VBox vBoxBottom = new VBox();
		HBox hb = new HBox();

		//Ta3 is main text output area 
		TextArea ta3 = new TextArea();
		ta3.setWrapText(true);
		ta3.autosize();
		ta3.setStyle("-fx-font-size: 15");

		//button for displaying list in most used order 
		RadioButton btn1 = new RadioButton("Most used list");
		btn1.setOnAction(e -> {
			
			ta3.clear();
			ta3.setText(sortByValue(mapMaker()));

		});
		//button for displaying list in alphabetical order 
		RadioButton btn2 = new RadioButton("Alphabetical list");
		btn2.setOnAction(e -> {
			
			ta3.clear();
			ta3.setText(countWords());

		});
		//button for displaying list in natural order 
		RadioButton btn3 = new RadioButton("Natural ordered list");
		btn3.setOnAction(e -> {
			
			ta3.clear();
			ta3.setText(countWordsInsertionOrder(btnCopyPaste.isSelected()));

		});
		
		//setting spacing on buttons 
		btn1.setPadding(new Insets(0, 15, 3, 0));
		btn2.setPadding(new Insets(0, 0, 3, 15));
		btn3.setPadding(new Insets(0, 0, 3, 15));
		hb.getChildren().addAll(btn1, btn2, btn3);
		//grouping the radio buttons together 
		ToggleGroup group2 = new ToggleGroup();
		btn1.setToggleGroup(group2);
		btn2.setToggleGroup(group2);
		btn3.setToggleGroup(group2);

		//adding hBox with radio buttons  and main text area to the bottom vBox 
		vBoxBottom.getChildren().addAll(hb, ta3);
		vBoxBottom.setPadding(new Insets(15, 15, 15, 15));

		// Set border pane ( main pane) 
		pane.setTop(vBoxCenter);
		pane.setCenter(vBox);
		pane.setBottom(vBoxBottom);

		// Create a scene and place it in the pane
		Scene scene = new Scene(pane, 600, 475);
		primaryStage.setTitle("Word Counter MEGA 1000.1");	//best name ever 
		primaryStage.setScene(scene);
		primaryStage.show();

		// text field for entering data
		textFieldForData.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					try {
						//resetting view buttons 
						btn1.setSelected(false);
						btn2.setSelected(false);

						// is a data is copy paste radio button the string getting from the text area is
						// a arrayList of strings
						if (btnCopyPaste.isSelected()) {
							listPath = textFieldForData.getText();
							mainString = listPath;
							ta3.setText(countWordsInsertionOrder(btnCopyPaste.isSelected()));
							textFieldForData.clear();
							
							// count the words in the main string
							searchResultsTextField.setText(mainString.length() + "   words  counted ");
							
							//resetting the text field 
							textFieldForData.clear();
						}
						
						// if the is file radio button is checked sPath is the string to name that file
						if (btnFileName.isSelected()) {
							
							// Getting the file name from the text field
							sPath = textFieldForData.getText();

							// creating a file object
							File f = new File(sPath);
							try {
								Scanner readIn = new Scanner(f);
								// Clearing main text area
								ta3.clear();
								String temp; // String to get string from file

								//reading info from the file 
								while (readIn.hasNext()) {	
									temp = readIn.next() + " ";
									builderString.append(temp);

									// Display file text to main area
									ta3.appendText(temp + "\n");

								}
								//setting string to be search able 
								fileString = builderString.toString();
								mainString = ta3.getText();

								textFieldForData.clear();
								
								// count the words in the main string
								searchResultsTextField.setText(mainString.length() + "   words  counted ");
								readIn.close();
							} catch (FileNotFoundException e) {
								ta3.clear();
								ta3.setText("No File with that name");
							}

						}
					} catch (NullPointerException ex) {
						System.out.println("Null pointer Exeption");
						ta3.clear();
						ta3.appendText("Please select a radio button ");
						textFieldForData.clear();
					}
				}

			}// end handle-----

		});	//end set on key pressed

	} //  end start method -------

	// Add words to arrayList
	public ArrayList<String> AddWords(String w) {
		ArrayList<String> list = new ArrayList<>();
		list.add(w);
		return list;
	}


	// method for counting words 
	public String countWords() {
		StringBuilder s = new StringBuilder();
		Map<String, Integer> map = new TreeMap<>();

		String[] words = mainString.split("[ \n \t \r , . ; : ! ? ( ) { } * - + @ #  $ % ^ & ]");
		for (int i = 0; i < words.length; i++) {
			String key = words[i].toLowerCase();

			if (key.length() > 0) {
				if (!map.containsKey(key)) {
					map.put(key, 1);
				} else {
					int value = map.get(key);
					value++;
					map.put(key, value);

				}
			}
		}

		Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
		for (Map.Entry<String, Integer> entry : entrySet) {
			s.append(entry.getKey() + "        " + entry.getValue() + " times\n");
		}
		String ss = s.toString();

		return ss;
	}
	
//method for sorting  words insertion order 
	public String countWordsInsertionOrder(boolean btnCopy) {
		StringBuilder s = new StringBuilder();
		LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
		// detecting what file to use for main string
		String[] words;
		if (btnCopy == true) {
			words = listPath.split("[ \n \t \r , . ; : ! ? ( ) { } * - + @ #  $ % '  ^ & ]");
		} else {
			words = mainString.split("[ \n \t \r , . ; : ! ? ( ) { } * - + @ #  $ %  ' ^ & ]");
		}
		for (int i = 0; i < words.length; i++) {
			String key = words[i].toLowerCase();

			if (key.length() > 0) {
				if (!map.containsKey(key)) {
					map.put(key, 1);
				} else {
					int value = map.get(key);
					value++;
					map.put(key, value);

				}
			}
		}

		Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
		for (Map.Entry<String, Integer> entry : entrySet) {
			s.append(entry.getKey() + "       " + entry.getValue() + " times\n");
		}
		String ss = s.toString();

		return ss;
	}

	// method for sorting words by value or most used first 
	public String sortByValue(TreeMap<String, Integer> map) {
		StringBuilder s = new StringBuilder();
		List<Entry<String, Integer>> list = new ArrayList<>(map.entrySet());
		list.sort(Entry.comparingByValue());

		Collections.reverse(list);
		for (Entry<String, Integer> entry : list) {
			s.append(entry.getKey() + "      " + entry.getValue() + " times\n");

		}
		String ss = s.toString();

		return ss;

	}

	public TreeMap<String, Integer> mapMaker() {
		TreeMap<String, Integer> map = new TreeMap<>();

		String[] words = mainString.split("[ \n \t \r , . ; : ! ? ( ) { } * - + @ ' #  $ % ^ & ]");
		for (int i = 0; i < words.length; i++) {
			String key = words[i].toLowerCase();

			if (key.length() > 0) {
				if (!map.containsKey(key)) {
					map.put(key, 1);
				} else {
					int value = map.get(key);
					value++;
					map.put(key, value);
				}
			}
		}
		return map;
	}
}// end class


//Class to create objects to display  in search area 
 class WordDisplays {
	public String result = "";
	public int position = 0; // Starting at 0 to count position
	
	public int size;

	// constructor for search for a word
	// s is the main string
	WordDisplays(String s, String searchWord) {
		String [] words = s.split("\\s");
		
		for (int i = 0; i < words.length; i++) {
			
			if (words[i].toLowerCase().equals (searchWord.toLowerCase())) {
					position = i;
			
			}
		if (s.toLowerCase().contains(searchWord.toLowerCase())) {
			result = searchWord;
		} else {
			result = "No match";
		}
		size = words.length;
	}
}
	
	public int getPosition() {
		return position;
	}

	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		if(!result.equals("No match") ) {
		return result + " is word number  "+ position + " in natural order";
		}
		else return "No match";
	}

}
