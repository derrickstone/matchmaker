/* Matcher implements the Stable Marriage algorithm
 * the objective is to create stable matches... match
 * all people with their most preferred mate (who will have them)
 * Some assumptions for this problem setup - 
 *   
 *   a potential partnership requires both parties to desire a relation
 *   simplified by having each participant rank all potential mates
 *   a stable match is a match between the 'best' preferred potential mate
 * 
 * 2/11/2018
 * Derrick Stone
 */
public class Matcher {
	
	/** The matcher class creates two arrays, one for the proposing 
	 * party and one for the proposee party
	 */
	Person[] proposers;
	Person[] proposees;
	
	/** the number of pairs */
	int numberOfMatches;
	
	/** control output */
	boolean verbose = false;
	
	/** constructor - initialize arrays of people
	 * and generate their match preferences
	 * @param num
	 */
	public Matcher(int num, boolean showOutput) {
		
		if (num <=0) {
			throw new IllegalArgumentException("Number of pairs must be a positive integer.");
		}
		proposers = new Person[num];
		proposees = new Person[num];
		numberOfMatches=num;
		verbose=showOutput;
		
		// generate some people for this match!
		for (int i = 0; i< numberOfMatches;i++) {
			proposers[i] = new Person("pat-"+i,numberOfMatches);
			proposees[i] = new Person("alex-"+i,numberOfMatches);
		}
	}
	
	/** run the match routine */
	public static void main(String[] args) {
		
		Matcher m = new Matcher(300,false);
		if (m.verbose) {
			System.out.println(m);
		}
		
		// now let's hold some matching rounds
		int count = 0;
		boolean unmatchedCouplesExist = true;
		 while ( unmatchedCouplesExist ) {
			
			m.makeProposals(); // good luck!
			
			m.considerProposals(); // accept or reject proposals
			
			unmatchedCouplesExist = false;
			for ( Person p : m.proposers) {
				if (p.hasMatch()==false) {
					unmatchedCouplesExist=true;
					break;
				}
			}
			
			count++;  // keep track of how much work is done
		}
		
		/** report on the outcome */
		System.out.println("Success! All couples matched!");
		if (m.verbose) {
			for ( int l=0;l<m.proposers.length;l++) {
				System.out.println(m.proposers[l].getName()+" matches with " + m.proposees[m.proposers[l].getMatch()].getName());
			}
		
			String word = "loop";
			if ( count > 1 ) {
				word = "loops";
			}
			System.out.println("Suitors confirmed matched in "+ count +" "+ word+".");
		}
	}
	
	/** return true if proposals are made */
	public void makeProposals() {  
		
		for (int i = 0; i<proposers.length;i++) {
			if ( proposers[i].hasMatch() == false ) {
				int targetIndex = proposers[i].setNextProposalTarget();
				int target = proposers[i].spousalPicks[targetIndex];
				if (verbose) {
					System.out.println(proposers[i].getName() + " proposes to "+ proposees[target].getName() );
				}
				proposees[ target ].addProposal(i);
			}
		}
	 
	}
		
	/** check the list of proposals- if any :( -and accept the most preferred
		 according to the preference order */
	public void considerProposals() {
		int bestPick = 0;
		for (int i=0;i<proposees.length;i++) { 
			for ( int p=0;p<proposees[i].spousalPicks.length;p++) {  //start search with most preferred
				bestPick = proposees[i].spousalPicks[p];
				if (proposees[i].proposals.contains(bestPick)) {
					//this represents the highest match 
					
					proposers[ bestPick ].proposalAccepted();
					if (verbose) {
						System.out.println(proposees[i].getName() +" accepts " + proposers[bestPick].getName());
					}
					break; // leave this loop
				}
				
			}
			// decline any other proposals
			for ( Integer q : proposees[i].proposals) {
				if ( q != bestPick) {
					proposers[q].proposalDeclined();
					if (verbose) {
						System.out.println(proposees[i].getName() + " declines "+ proposers[q].getName());
					}
				}
			}
			// clear out the proposals
			proposees[i].proposals.clear();
			// save the accepted proposal
			proposees[i].addProposal(bestPick);
		
		}
	}
	public String toString() {
		String returnString = "Proposers\n";
		for (Person p : proposers) {
			returnString+=p.toString();
		}
		returnString+="\nProposees\n";
		for (Person p : proposees) {
			returnString+=p.toString();
		}
		return returnString;
	}
}
