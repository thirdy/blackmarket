package io.jexiletools.es.model.json;

import com.google.gson.annotations.SerializedName;

public class Sockets {
	Sockets.SortedLinkGroup sortedLinkGroup;
	String allSockets;
    String allSocketsSorted;
    Integer largestLinkGroup;
    Integer socketCount;
    
	@Override
	public String toString() {
		return "Sockets [sortedLinkGroup=" + sortedLinkGroup + ", allSockets=" + allSockets + ", allSocketsSorted="
				+ allSocketsSorted + ", largestLinkGroup=" + largestLinkGroup + ", socketCount=" + socketCount
				+ "]";
	}
	
	public Sockets.SortedLinkGroup getSortedLinkGroup() {
		return sortedLinkGroup;
	}

	public void setSortedLinkGroup(Sockets.SortedLinkGroup sortedLinkGroup) {
		this.sortedLinkGroup = sortedLinkGroup;
	}

	public String getAllSockets() {
		return allSockets;
	}

	public void setAllSockets(String allSockets) {
		this.allSockets = allSockets;
	}

	public String getAllSocketsSorted() {
		return allSocketsSorted;
	}

	public void setAllSocketsSorted(String allSocketsSorted) {
		this.allSocketsSorted = allSocketsSorted;
	}

	public Integer getLargestLinkGroup() {
		return largestLinkGroup;
	}

	public void setLargestLinkGroup(Integer largestLinkGroup) {
		this.largestLinkGroup = largestLinkGroup;
	}

	public Integer getSocketCount() {
		return socketCount;
	}

	public void setSocketCount(Integer socketCount) {
		this.socketCount = socketCount;
	}

	public static class SortedLinkGroup {
		@SerializedName("1") String lg1;
		@SerializedName("2") String lg2;
		@SerializedName("3") String lg3;
		@SerializedName("4") String lg4;
		@SerializedName("5") String lg5;
		@Override
		public String toString() {
			return "SortedLinkGroup [lg1=" + lg1 + ", lg2=" + lg2 + ", lg3=" + lg3 + ", lg4=" + lg4 + ", lg5=" + lg5
					+ "]";
		}
		public String getLg1() {
			return lg1;
		}
		public void setLg1(String lg1) {
			this.lg1 = lg1;
		}
		public String getLg2() {
			return lg2;
		}
		public void setLg2(String lg2) {
			this.lg2 = lg2;
		}
		public String getLg3() {
			return lg3;
		}
		public void setLg3(String lg3) {
			this.lg3 = lg3;
		}
		public String getLg4() {
			return lg4;
		}
		public void setLg4(String lg4) {
			this.lg4 = lg4;
		}
		public String getLg5() {
			return lg5;
		}
		public void setLg5(String lg5) {
			this.lg5 = lg5;
		}
		
		
	}
	
	
}