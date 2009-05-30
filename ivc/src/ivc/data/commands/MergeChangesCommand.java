/**
 * 
 */
package ivc.data.commands;


/**
 * @author danielan
 * 
 */
public class MergeChangesCommand implements CommandIntf {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ivc.data.command.CommandIntf#execute(ivc.data.command.CommandArgs)
	 */
	@Override
	public Result execute(CommandArgs args) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * merge:H H := []; append(RCL, H); UL := []; append(LL, UL); for (i:=0;
	 * i<|RUL|; i++) append(RUL[i], UL); for (i:=0; i<|UL|; i++) { O := UL[i];
	 * j:=0; while (j<|H| and H[j]!O) j++; O0 := transformSOCT2(O,
	 * sublist(H,j,|H|)); append(O0, H); } return H;
	 */
	private void merge() {

	}

}
