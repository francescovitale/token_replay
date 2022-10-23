package FileSystemAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import PMLogic.ProcessModel;
import PMLogic.TRParameter;

public class TRParameterReader {
	
	private String ConfigurationParametersDirectory;
	
	public TRParameterReader(String Dir) {
		ConfigurationParametersDirectory = Dir;
	}


	public ArrayList<TRParameter> getTRParameterList(ProcessModel PM) throws FileNotFoundException {
		
		ArrayList<TRParameter> TRPList = new ArrayList<TRParameter>();
		
		File TRParameterFile = new File(ConfigurationParametersDirectory+ "\\" + PM.getName() + "\\parameters");
		Scanner Reader = new Scanner(TRParameterFile);
		
		while(Reader.hasNextLine()) {
			String TRParameterString = Reader.nextLine();
			String [] TRParameterSplitString = TRParameterString.split("\\:");
			TRPList.add(new TRParameter(TRParameterSplitString[0], Float.parseFloat(TRParameterSplitString[1]),PM));
		}
		
		Reader.close();
		
		
		return TRPList;
		
		
	}

}
