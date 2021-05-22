
import java.util.logging.Level;
import java.util.logging.Logger;

public class Expression extends ExpressionTree {
   
   public String fullyParenthesized() {
      // add implementation here
      String result = this.getFullyExpression((BNode<String>)this.root());
      
      return result;
   }
   public String getFullyExpression(BNode<String> node)
   {
       if(node.left == null && node.right == null)
       {
           return node.data;
       }
       String left = this.getFullyExpression(node.left);
       String right = this.getFullyExpression(node.right);
       return "("+left+node.data+right+")";
   }

   public Expression(String s) {
      super();
      // add implementation here
      //normalize string
      String normalizeString = s.replace("(", " ( ");
      normalizeString = normalizeString.replace("  "," ");
      normalizeString = normalizeString.replace(")"," ) ");
      normalizeString = normalizeString.replace("  "," ");
      normalizeString = normalizeString.trim();
      this.processExpression(normalizeString, null, true);
   }
   private void processExpression(String exp, BNode<String> node, boolean isLeft)
   {
       //find operation pos which can seperate two parts
       String[] exps = exp.split(" ");
       
       //"()"
       boolean closedWithBracket = false;
       if(exp.charAt(0) == '(')
        {
            int bracketCount = 1;
            for(int i=1;i< exp.length();i++)
            {
                if(exp.charAt(i) == '(')
                {
                    bracketCount++;
                }
                if(exp.charAt(i) == ')')
                {
                    bracketCount--;
                }
                if(bracketCount == 0)
                {
                    if( i == exp.length()-1)
                    {
                        closedWithBracket = true;
                    }
                    break;
                }
            }
        }
       
       if(closedWithBracket)
       {
           this.processExpression(exp.substring(2, exp.length()-2), node, isLeft);
           return;
       }
       if(!exp.contains(" ") && node != null)
       {
            if(isLeft)
            {
                BNode<String> newNode = new BNode<String>(exp,node,null,null);
                node.setLeft(newNode);
            }
            else
            {
                BNode<String> newNode = new BNode<String>(exp,node,null,null);
                node.setRight(newNode);
            }
           return;
       }
       //process "+,-"
       int matchedBracket = 0;
       for(int loopIndex = 0;loopIndex < 2;loopIndex++)
       {
            String operation1 = "+";
            String operation2 = "-";
            if(loopIndex == 1)
            {
                 operation1 = "*";
                 operation2 = "/";
            }
            matchedBracket = 0;
            for(int i=exps.length-1; i >= 0;i--)
            {
                if(exps[i].equals(")"))
                {
                    matchedBracket++;
                }
                if(exps[i].equals("("))
                {
                    matchedBracket--;
                }
                if(matchedBracket == 0 && ( exps[i].equals(operation1) || exps[i].equals(operation2)))
                {
                    //seperate with i
                    String left = "";
                    String right = "";
                    for(int j = 0;j<i;j++)
                    {
                        if(j == 0)
                        {
                            left += exps[j];
                        }
                        else
                        {
                            left += " "+exps[j];
                        }
                        
                    }
                    for(int j = i+1;j<exps.length;j++)
                    {
                        if(j == i+1)
                        {
                            right += exps[j];
                        }
                        else{
                            right += " "+exps[j];
                        }
                        
                    }

                    BNode<String> newNode = null;
                    if(node == null)
                    {
                        try {
                            this.addRoot(exps[i]);
                        } catch (Exception ex) {
                            Logger.getLogger(Expression.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        newNode = (BNode<String>)this.root();
                    }
                    else if(isLeft)
                    {
                        newNode = new BNode<String>(exps[i],node,null,null);
                        node.setLeft(newNode);
                    }
                    else
                    {
                        newNode = new BNode<String>(exps[i],node,null,null);
                        node.setRight(newNode);
                    }
                    //System.out.print(left+"-left,");
                    //System.out.print(right+"-right,");
                    this.processExpression(left, newNode, true);
                    this.processExpression(right, newNode, false);
                    return;
                }
            }
       }
       
       
   }
   public double evaluate() {
      // add implementation here
      
      return this.evaluateNode((BNode<String>)this.root());
   }
   public double evaluateNode(BNode<String> node)
   {
       double result = 0.0;
       if(node.left == null && node.right == null)
       {
           return Double.parseDouble(node.data);
       }
       double left = this.evaluateNode(node.left);
       double right = this.evaluateNode(node.right);
       if(node.data.equals("+"))
       {
           result = left + right;
       }
       else if(node.data.equals("-"))
       {
           result = left - right;
       }
       else if(node.data.equals("*"))
       {
           result = left * right;
       }
       else if(node.data.equals("/"))
       {
           result = left / right;
       }
       return result;
   }
}
