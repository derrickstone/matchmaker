import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Person {
	/** just for fun */
	String name;
	/** array of match preferences with the most preferred first */
	int[] spousalPicks;  
	/** responses integer array holds collected proposal responses
	 * used by the proposer type to manage workflow
	 * key: 0 unasked, 1 asked, 2 no, 3 maybe */
	int[] responses; 
	
	/** proposals list used by the proposees to manage their propositions */
	List<Integer> proposals;
	
	public Person(String name, int numberOfPairs) {
		this.name = name;
		this.responses = new int[numberOfPairs];
		this.proposals = new ArrayList<Integer>();
		setPreferredMatches(numberOfPairs);
	}
	
	/** generate 'random' match preferences */
	public void setPreferredMatches(int numberOfPairs) {
		spousalPicks = new int[numberOfPairs];
		for ( int x = 0; x< numberOfPairs;x++ ) {  // create a sequential array
				spousalPicks[x]=x;	
		}
		
		shuffleArray(spousalPicks);  // shuffle
		
	}
	
	/** proposee functions */
	public void addProposal(int suitor) {
		proposals.add(suitor);
	}
	
	/** propser functions */
	public void proposalAccepted() {
		for (int i=0;i<responses.length;i++) {
			if (responses[i]==1) {
				responses[i]=3;
				return;
			}
		}
	}
	public void proposalDeclined() {  // so sad.
		for (int i=0;i<responses.length;i++) {
			if (responses[i]==1) {
				responses[i]=2;
				return;
			}
		}
	}
	public int getMatch() {
		for (int i=0; i<responses.length;i++) {
			if (responses[i]==3) {  // an acceptance!
				return spousalPicks[i];
			}
		}
		return -1; //no match found
	}
	public boolean hasMatch() {
		for (int i=0; i<responses.length;i++) {
			if (responses[i]==3) {  // an acceptance!
				return true;
			}
		}
		return false;
	}
	public int setNextProposalTarget() {
		for ( int p=0;p<responses.length;p++ ) {
			if (responses[p] ==0) {
				// propose to this lucky person
				responses[p]=1;
				return p;
			}
		}
		//System.out.println("Proposer is out of potential mates");
		throw new IllegalArgumentException("Something is wrong -we ran out of matches for someone.");
		//return -1; // no proposals to make!
	}
	
	// setters
	void setName(String name) {
		this.name = name;
	}

	
	// toString
	@Override
	public String toString() {
		String returnString = this.name+" preference order ";
		for (int i=0;i<this.spousalPicks.length;i++) {
			returnString = returnString+ " "+this.spousalPicks[i];
		}	
		returnString+="\n";
		return returnString;
	}
	
	public String getName() {
		return this.name;
	}

	private void shuffleArray(int[] array)
	{
	    int index;
	    Random random = new Random();
	    for (int i = array.length - 1; i > 0; i--)
	    {
	        index = random.nextInt(i + 1);
	        if (index != i)
	        {
	            array[index] ^= array[i];
	            array[i] ^= array[index];
	            array[index] ^= array[i];
	        }
	    }
	}
}
