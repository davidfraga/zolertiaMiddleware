package br.ufpe.gprt.zolertia.device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

public class RadioManager {
	// TODO Setting the initial parameters
	private static int RSSIThreshold = -29;

	private static long minTimeToChange = 30000;
	// TODO static class. store the state of the zolertias

	List<Integer> top5Channels = new ArrayList<Integer>();

	public void analizeChannels(List<Integer> channels) {
		Collections.sort(channels);
		boolean updated = true;
		if (top5Channels.size() != 5)
			updated = false;
		else {
			for (int i = 0; i < 5; i++) {
				if (channels.get(i) != top5Channels.get(i)) {
					updated = false;
					break;
				}
			}
		}
		if (!updated) {
			top5Channels.clear();
			top5Channels.addAll(channels.subList(0, 4));
		}
	}

	public List<Integer> getTop5Channels() {
		return top5Channels;
	}

}
