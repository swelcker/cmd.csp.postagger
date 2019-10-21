package csppostagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;


public class CSPPOSTagger
{
	public CSPPOSNode root;

	public CSPPOSTagger()
	{
	}

	public CSPPOSTagger(CSPPOSNode node)
	{
		root = node;
	}
	public void constructTreeFromLanguage(String strLanguage)
			throws IOException
		{
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
				this.getClass().getResourceAsStream("/cmd/csp/postagger/Models/"+strLanguage.toLowerCase()+".RDR"), "UTF-8"));
			
			//System.out.println("constructTreeFromLanguage sf: "+strLanguage);
		
			String line = buffer.readLine();

			this.root = new CSPPOSNode(new CSPPOSFWObject(false), "NN", null, null, null, 0);

			CSPPOSNode currentNode = this.root;
			int currentDepth = 0;

			for (; (line = buffer.readLine()) != null;) {
				int depth = 0;
				for (int i = 0; i <= 6; i++) { // Supposed that the maximum
												// exception level is up to 6.
					if (line.charAt(i) == '\t')
						depth += 1;
					else
						break;
				}

				line = line.trim();
				if (line.length() == 0)
					continue;

				if (line.contains("cc:"))
					continue;

				CSPPOSFWObject condition = Utils
					.getCondition(line.split(" : ")[0].trim());
				String conclusion = Utils.getConcreteValue(line.split(" : ")[1]
					.trim());

				CSPPOSNode node = new CSPPOSNode(condition, conclusion, null, null, null, depth);

				if (depth > currentDepth) {
					currentNode.setExceptNode(node);
				}
				else if (depth == currentDepth) {
					currentNode.setIfnotNode(node);
				}
				else {
					while (currentNode.depth != depth)
						currentNode = currentNode.fatherNode;
					currentNode.setIfnotNode(node);
				}
				node.setFatherNode(currentNode);

				currentNode = node;
				currentDepth = depth;
			}
			buffer.close();
		}
	public void constructTreeFromRulesFile(String rulesFilePath)
		throws IOException
	{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
			new FileInputStream(new File(rulesFilePath)), "UTF-8"));
		String line = buffer.readLine();

		this.root = new CSPPOSNode(new CSPPOSFWObject(false), "NN", null, null, null, 0);

		CSPPOSNode currentNode = this.root;
		int currentDepth = 0;

		for (; (line = buffer.readLine()) != null;) {
			int depth = 0;
			for (int i = 0; i <= 6; i++) { // Supposed that the maximum
											// exception level is up to 6.
				if (line.charAt(i) == '\t')
					depth += 1;
				else
					break;
			}

			line = line.trim();
			if (line.length() == 0)
				continue;

			if (line.contains("cc:"))
				continue;

			CSPPOSFWObject condition = Utils
				.getCondition(line.split(" : ")[0].trim());
			String conclusion = Utils.getConcreteValue(line.split(" : ")[1]
				.trim());

			CSPPOSNode node = new CSPPOSNode(condition, conclusion, null, null, null, depth);

			if (depth > currentDepth) {
				currentNode.setExceptNode(node);
			}
			else if (depth == currentDepth) {
				currentNode.setIfnotNode(node);
			}
			else {
				while (currentNode.depth != depth)
					currentNode = currentNode.fatherNode;
				currentNode.setIfnotNode(node);
			}
			node.setFatherNode(currentNode);

			currentNode = node;
			currentDepth = depth;
		}
		buffer.close();
	}

	public CSPPOSNode findFiredNode(CSPPOSFWObject object)
	{
		CSPPOSNode currentN = root;
		CSPPOSNode firedN = null;
		while (true) {
			if (currentN.satisfy(object)) {
				firedN = currentN;
				if (currentN.exceptNode == null) {
					break;
				}
				else {
					currentN = currentN.exceptNode;
				}
			}
			else {
				if (currentN.ifnotNode == null) {
					break;
				}
				else {
					currentN = currentN.ifnotNode;
				}
			}

		}

		return firedN;
	}

	public String tagInitializedSentence(String inInitializedSentence)
	{
		StringBuilder sb = new StringBuilder();
		List<CSPPOSWordTag> wordtags = Utils.getWordTagList(inInitializedSentence);
		int size = wordtags.size();
		for (int i = 0; i < size; i++) {
			CSPPOSFWObject object = Utils.getObject(wordtags, size, i);
			CSPPOSNode firedNode = findFiredNode(object);
			if (firedNode.depth > 0)
				sb.append(wordtags.get(i).word + "/" + firedNode.conclusion
					+ " ");
			else {
				// Fired at root, return initialized tag.
				sb.append(wordtags.get(i).word + "/" + wordtags.get(i).tag
					+ " ");
			}
		}
		return sb.toString();
	}

	public void tagInitializedCorpus(String inInitializedFilePath)
		throws IOException
	{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
			new FileInputStream(new File(inInitializedFilePath)), "UTF-8"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(inInitializedFilePath + ".TAGGED"), "UTF-8"));

		for (String line; (line = buffer.readLine()) != null;) {
			line = line.replace("“", "''").replace("”", "''")
				.replace("\"", "''").trim();
			if (line.length() == 0) {
				bw.write("\n");
				continue;
			}
			bw.write(tagInitializedSentence(line) + "\n");
		}
		buffer.close();
		bw.close();
	}

	public String tagVnSentence(HashMap<String, String> FREQDICT,
		String sentence)
		throws IOException
	{
		StringBuilder sb = new StringBuilder();

		String line = sentence.trim();
		if (line.length() == 0) {
			return "\n";
		}

		List<CSPPOSWordTag> wordtags = CSPPOSInitialTagger.VnInitTagger4Sentence(FREQDICT, line);

		int size = wordtags.size();
		for (int i = 0; i < size; i++) {
			CSPPOSFWObject object = Utils.getObject(wordtags, size, i);
			CSPPOSNode firedNode = findFiredNode(object);
			sb.append(wordtags.get(i).word + "/" + firedNode.conclusion + " ");
		}

		return sb.toString();
	}

	public void tagVnCorpus(HashMap<String, String> FREQDICT,
		String inRawFilePath)
		throws IOException
	{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
			new FileInputStream(new File(inRawFilePath)), "UTF-8"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(inRawFilePath + ".TAGGED"), "UTF-8"));
		for (String line; (line = buffer.readLine()) != null;) {
			bw.write(tagVnSentence(FREQDICT, line) + "\n");
		}
		buffer.close();
		bw.close();
	}

	public String tagEnSentence(HashMap<String, String> FREQDICT,
		String sentence)
		throws IOException
	{
		StringBuilder sb = new StringBuilder();

		String line = sentence.trim();
		if (line.length() == 0) {
			return "\n";
		}

		List<CSPPOSWordTag> wordtags = CSPPOSInitialTagger.EnInitTagger4Sentence(FREQDICT, line);

		int size = wordtags.size();
		for (int i = 0; i < size; i++) {
			CSPPOSFWObject object = Utils.getObject(wordtags, size, i);
			CSPPOSNode firedNode = findFiredNode(object);
			sb.append(wordtags.get(i).word + "/" + firedNode.conclusion + " ");
		}
		return sb.toString();
	}

	public void tagEnCorpus(HashMap<String, String> FREQDICT,
		String inRawFilePath)
		throws IOException
	{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
			new FileInputStream(new File(inRawFilePath)), "UTF-8"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(inRawFilePath + ".TAGGED"), "UTF-8"));
		for (String line; (line = buffer.readLine()) != null;) {
			bw.write(tagEnSentence(FREQDICT, line) + "\n");
		}
		buffer.close();
		bw.close();
	}

	public String tagSentence(HashMap<String, String> FREQDICT, String sentence)
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		String line = sentence.trim();
		if (line.length() == 0) {
			return "\n";
		}

		List<CSPPOSWordTag> wordtags = CSPPOSInitialTagger.InitTagger4Sentence(FREQDICT, line);

		int size = wordtags.size();
		for (int i = 0; i < size; i++) {
			CSPPOSFWObject object = Utils.getObject(wordtags, size, i);
			CSPPOSNode firedNode = findFiredNode(object);
			if (firedNode.depth > 0)
				sb.append(wordtags.get(i).word + "/" + firedNode.conclusion
					+ " ");
			else {
				// Fired at root, return initialized tag.
				sb.append(wordtags.get(i).word + "/" + wordtags.get(i).tag
					+ " ");
			}
		}
		return sb.toString();
	}

	public void tagCorpus(HashMap<String, String> FREQDICT, String inRawFilePath)
		throws IOException
	{
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
			new FileInputStream(new File(inRawFilePath)), "UTF-8"));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
			new FileOutputStream(inRawFilePath + ".TAGGED"), "UTF-8"));
		for (String line; (line = buffer.readLine()) != null;) {
			bw.write(tagSentence(FREQDICT, line) + "\n");
		}
		buffer.close();
		bw.close();

	}



	public static void run(String args[])
		throws IOException
	{
		try {

					CSPPOSTagger tree = new CSPPOSTagger();
					System.out.println("\nRead a POS tagging model from: "
						+ args[0]);
					tree.constructTreeFromRulesFile(args[0]);
					System.out.println("Read a lexicon from: " + args[1]);
					HashMap<String, String> FREQDICT = Utils
						.getDictionary(args[1]);
					System.out.println("Perform POS tagging on:" + args[2]);
					tree.tagCorpus(FREQDICT, args[2]);

		}
		catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}

}
