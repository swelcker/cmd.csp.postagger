package cmd.csp.postagger;


public class CSPPOSNode
{
	public CSPPOSFWObject condition;
	public String conclusion;
	public CSPPOSNode exceptNode;
	public CSPPOSNode ifnotNode;
	public CSPPOSNode fatherNode;
	int depth;

	public CSPPOSNode(CSPPOSFWObject inCondition, String inConclusion, CSPPOSNode inFatherNode,
		CSPPOSNode inExceptNode, CSPPOSNode inIfnotNode, int inDepth)
	{
		this.condition = inCondition;
		this.conclusion = inConclusion;
		this.fatherNode = inFatherNode;
		this.exceptNode = inExceptNode;
		this.ifnotNode = inIfnotNode;
		this.depth = inDepth;
	}

	public void setIfnotNode(CSPPOSNode node)
	{
		this.ifnotNode = node;
	}

	public void setExceptNode(CSPPOSNode node)
	{
		this.exceptNode = node;
	}

	public void setFatherNode(CSPPOSNode node)
	{
		this.fatherNode = node;
	}

	public int countNodes()
	{
		int count = 1;
		if (exceptNode != null) {
			count += exceptNode.countNodes();
		}
		if (ifnotNode != null) {
			count += ifnotNode.countNodes();
		}
		return count;
	}

	public boolean satisfy(CSPPOSFWObject object)
	{
		boolean check = true;
		for (int i = 0; i < 13; i++) {
			String key = condition.context[i];
			if (key != null) {
				if (!key.equals(object.context[i])) {
					check = false;
					break;
				}
			}
		}
		return check;
	}
}
