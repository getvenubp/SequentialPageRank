import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @author Venu
 *
 */
public class PageRankSequential {

	static HashMap<Integer, ArrayList<Integer>> adjacencyMatrix = new HashMap<Integer, ArrayList<Integer>>();
	static LinkedHashMap <Integer,Double> sortedPageRank = new LinkedHashMap <Integer,Double>();
	
	/**
	 * Parses the input file and initializes the adjacency matrix
	 */
	public static void initializeAdjacencyMatrix(String inputFileName) throws IOException{
		
		FileReader fr=null;
		BufferedReader br = null;
		try{
			fr= new FileReader(inputFileName);
			br = new BufferedReader(fr);
			
			String str;
			Integer nodeNum = null;
			while((str=br.readLine())!=null){
				ArrayList <Integer> intermediateList = new ArrayList <Integer>();
				String stringRead[]=str.split(" ");
				nodeNum = Integer.parseInt(stringRead[0]);
				for(int i=1;i<stringRead.length;i++){
					intermediateList.add(Integer.parseInt(stringRead[i]));
				}
				adjacencyMatrix.put(nodeNum,intermediateList);
			}
		}
		catch(FileNotFoundException fe){
			System.out.println("File Not Found Exception");
			fe.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception in opening a file");
			e.printStackTrace();
		}
		finally{
			br.close();
			fr.close();
		}
	}

	/**
	 * Assigns initial rank values for each page in rankValuesTable as 1/N
	 */
	public static void initialRankTable(HashMap<Integer,Double> rankValuesTable, int numOfUrls){
	   
	   double initialRankValuePerPage = 1.0/(double)numOfUrls;
	   
	   for (int i=0;i<numOfUrls;i++)
			rankValuesTable.put(i, initialRankValuePerPage);
	}

	/**
	 * Calculates page rank values for each page using an intermediate table
	 */
	public static void calculatePageRank(HashMap<Integer,Double> rankValuesTable,int numOfUrls,double dampingFactor){ 
		
		Iterator<Integer> iterater = adjacencyMatrix.keySet().iterator();
		ArrayList<Integer> targetUrlsList;
		HashMap<Integer,Double> intermediateTable = new HashMap<Integer,Double>(); 
		int sourceUrl=0,targetUrl=0;
		double intermediateRankValue=0.0,danglingValue=0.0;
		
		for(int i=0;i<numOfUrls;i++)
			intermediateTable.put(i, 0.0000000);
		
		while(iterater.hasNext()){
			sourceUrl = ((Integer)iterater.next()).intValue();
			targetUrlsList = adjacencyMatrix.get(sourceUrl);
			int outdegreeOfSourceUrl = targetUrlsList.size();
			for (int i=0; i<outdegreeOfSourceUrl;i++){
		    	targetUrl = targetUrlsList.get(i).intValue();
		    	intermediateRankValue = intermediateTable.get(targetUrl)+ rankValuesTable.get(sourceUrl)/(double)outdegreeOfSourceUrl;
		    	intermediateTable.put(targetUrl, intermediateRankValue);		    	   
		    }
			if (outdegreeOfSourceUrl == 0){
		        danglingValue += rankValuesTable.get(sourceUrl);
			}
		}
		
		double danglingValuePerPage = danglingValue / (double)numOfUrls;
		       
		for (int i=0;i<numOfUrls;i++)
		    	   rankValuesTable.put(i,intermediateTable.get(i)+danglingValuePerPage);
		       		
		for (int i=0;i<numOfUrls;i++)
		    	   rankValuesTable.put(i, dampingFactor*rankValuesTable.get(i)+(1-dampingFactor)*(1.0/(double)numOfUrls));	
	}
	
	/**
	 * Sorts urls according to calculated page rank
	 */
	public static void sortPageRank(HashMap<Integer,Double> rankValuesTable){
		
		ArrayList<Integer> rankValuesTableKeys = new ArrayList<Integer>(rankValuesTable.keySet());
		ArrayList<Double> rankValuesTableValues = new ArrayList<Double>(rankValuesTable.values());
		Collections.sort(rankValuesTableValues,Collections.reverseOrder());
		Iterator<Double> valueIterator = rankValuesTableValues.iterator();
		
		while(valueIterator.hasNext()){
			Double value = valueIterator.next();
			Iterator<Integer> keyIterator = rankValuesTableKeys.iterator();
			while(keyIterator.hasNext()){
				Integer key = keyIterator.next();
				Double pageRankValue = rankValuesTable.get(key);
				if(value.equals(pageRankValue)){
					rankValuesTable.remove(key);
					rankValuesTableKeys.remove(key);
					sortedPageRank.put(key, value);
					break;
				}
			}
		}
	}
	
	/**
	 * Writes final results onto an output file
	 */
	public static void outputPageRank(String outputFileName) throws IOException{
		
		FileWriter file=null;
		BufferedWriter out =null;
		try{
			file = new FileWriter(outputFileName);
			out = new BufferedWriter(file);
			ArrayList<Integer> sortedPageRankKeys = new ArrayList<Integer>(sortedPageRank.keySet());
			Iterator<Integer> iterator = sortedPageRankKeys.iterator();
			out.write("Top 10 Page Ranked URLs are:\n");
			out.write("----------------------------\n");
			out.write("URL\tPageRank value\n");
			out.write("----------------------------\n");
			int i=0;
			double sum=0.0;
			while(iterator.hasNext()){
				Integer key = iterator.next();
				if(i<10){
					out.write(key+"\t"+sortedPageRank.get(key)+"\n");
					i++;
				}
				System.out.println(key+" "+sortedPageRank.get(key));
				sum+=sortedPageRank.get(key);
			}
			out.write("----------------------------\n");
			System.out.print("\nSum of all page rank is "+sum);
		}
		catch(FileNotFoundException fe){
			System.out.println("File Not Found Exception\n");
			fe.printStackTrace();
		}
		catch(Exception e){
			System.out.println("Exception caught\n");
			e.printStackTrace();
		}
		finally{
			out.close();
			file.close();
		}
	}
	
	/**
	 * Main Method 
	 */
	public static void main(String[] args) throws IOException {
		
		if(args.length!=4){
		System.out.println("\njava PageRankSequential [input file name] [output file name] [iteration count] [damping factor]");
		System.exit(-1);
		}
		
		String inputFileName = args[0];
		String outputFileName = args[1];
		int iterationCount = Integer.parseInt(args[2]);
		double dampingFactor = Double.parseDouble(args[3]);
		
		if(iterationCount<0){
		System.out.println("\n[iteration count] cannot be less than zero");
		System.exit(-1);
		}
		
		if(dampingFactor<=0 || dampingFactor>=1){
		System.out.println("\n[damping factor] should be between 0 and 1");
		System.exit(-1);
		}
		
		HashMap<Integer,Double> rankValuesTable = new HashMap<Integer,Double>();
		int numOfUrls=0;
				
		long start = System.currentTimeMillis();
		
		initializeAdjacencyMatrix(inputFileName);
		numOfUrls=adjacencyMatrix.size();
		initialRankTable(rankValuesTable,numOfUrls);
		
		for(int i=0;i<iterationCount;i++)
			calculatePageRank(rankValuesTable,numOfUrls,dampingFactor);
		
		sortPageRank(rankValuesTable);
		outputPageRank(outputFileName);
		
		long finish = System.currentTimeMillis();
		System.out.println("\n\nTop 10 URL has been written to output file : "+outputFileName);
		System.out.println("\nTime taken to calculate page rank is "+(finish - start)/1000.0+" sec");
		
	}//end of main
}//end of class
