
import java.util.logging.Level;
import java.util.logging.Logger;

public class Expression extends ExpressionTree {
   
   public String fullyParenthesized() {
      // add implementation here
      String result = this.getFullyExpression((BNode<String>)this.root());
      
      return result;
   }
   
   // this is recursion fucntion that return fully expression string
   public String getFullyExpression(BNode<String> node)
   {
       //if it is leaf, return it
       if(node.left == null && node.right == null)
       {
           return node.data;
       }
       //left expression
       String left = this.getFullyExpression(node.left);
       
       //right expression
       String right = this.getFullyExpression(node.right);
       
       //sum of left and right
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
      
      normalizeString = normalizeString.replace("/"," / ");
      normalizeString = normalizeString.replace("  "," ");
      
      normalizeString = normalizeString.trim();
      
      
      this.processExpression(normalizeString, null, true);
   }
   
   //Recursion function
   //this method analyzes expression recursively
   // expression = expressionLeft + operation + expressionRight
   private void processExpression(String exp, BNode<String> node, boolean isLeft)
   {
       //splits exp with white space
       String[] exps = exp.split(" ");
       
       //check (expression)
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
       
       //return without bracket
       if(closedWithBracket)
       {
           this.processExpression(exp.substring(2, exp.length()-2), node, isLeft);
           return;
       }
       
       //if exp is leaf, return this only
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
       
       //analyze expression in order with + - * /
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
            
            //analyze expression with two operation (+,- or * /)
            for(int i=exps.length-1; i >= 0;i--)
            {
                //check bracket pair
                if(exps[i].equals(")"))
                {
                    matchedBracket++;
                }
                if(exps[i].equals("("))
                {
                    matchedBracket--;
                }
                
                //analyze expression with two operation (+,- or * /)
                if(matchedBracket == 0 && ( exps[i].equals(operation1) || exps[i].equals(operation2)))
                {
                    //get seperated two expressions
                    
                    String left = "";
                    String right = "";
                    
                    //get left expression
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
                    
                    //get right expression
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

                    //create new Node
                    BNode<String> newNode = null;
                    
                    //root node
                    if(node == null)
                    {
                        try {
                            this.addRoot(exps[i]);
                        } catch (Exception ex) {
                            Logger.getLogger(Expression.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        newNode = (BNode<String>)this.root();
                    }
                    //left node
                    else if(isLeft)
                    {
                        newNode = new BNode<String>(exps[i],node,null,null);
                        node.setLeft(newNode);
                    }
                    //right node
                    else
                    {
                        newNode = new BNode<String>(exps[i],node,null,null);
                        node.setRight(newNode);
                    }
                    
                    //call and analyze recursion func with left expression
                    this.processExpression(left, newNode, true);
                    
                    //call and analyze recursion func with right expression
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
   
   //this is recursion function that evaluate expression of node
   public double evaluateNode(BNode<String> node)
   {
       double result = 0.0;
       
       //if it is leaf, return it as double
       if(node.left == null && node.right == null)
       {
           return Double.parseDouble(node.data);
       }
       //left value of node
       double left = this.evaluateNode(node.left);
       
       //right value of node
       double right = this.evaluateNode(node.right);
       
       //calculate with left and right
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
