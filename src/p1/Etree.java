package p1;
import java.util.*;


public class Etree
{
    private String infixExpression, postfixExpression;
    private Node tree;
    private Stack< Character > stack;
    private double Arr[];
    
    public String getPostfix(){ return postfixExpression; }
    public Node getTree() { return tree; }
    
    public Etree(String exp)
    {
        infixExpression = new String("");
        postfixExpression = new String("");
        Arr = new double[26];
        setInfixExpression(exp);
    }
    
    public boolean setInfixExpression (String a) 
    { 
        infixExpression = a; 
        postfixExpression = "";
        if( infixToPostfix() == false)
            return false;
        return createExpressionTree();
    }
    
    public void setArray(double arr[])
    {
    	for(int i = 0; i < 26; ++i)
    		Arr[i] = arr[i];
    }
    
    public double Evaluate(double arr[])
    {
    	setArray(arr);
        return EvaluateHelper(tree);
    }
    private double EvaluateHelper(Node node)
    {
        if(Character.isDigit( node.val.charAt(0) ) )
            return Double.parseDouble(node.val);
        if(Character.isLetter( node.val.charAt(0) ) )
            return Arr[ node.val.charAt(0) - 'a' ];
        
        if(node.val.charAt(0) == '+')
            return EvaluateHelper(node.left) + EvaluateHelper(node.right); 
        
        if(node.val.charAt(0) == '-')
          return EvaluateHelper(node.left) - EvaluateHelper(node.right);
        
        if(node.val.charAt(0) == '*')
            return EvaluateHelper(node.left) * EvaluateHelper(node.right);
        
        if(node.val.charAt(0) == '/')
            return EvaluateHelper(node.left) / EvaluateHelper(node.right);
        
        return Math.pow( EvaluateHelper(node.left), EvaluateHelper(node.right));
    }
    private boolean createExpressionTree()
    {
        Stack <Node> st = new Stack<>();
        char tempC;
        Node temp;
        for(int i = 0, n = postfixExpression.length(); i < n; ++i)
        {
            tempC = postfixExpression.charAt(i);
            if( operandCheck(tempC) )
            {
                temp = new Node(postfixExpression.substring(i, i + 1));
                if(st.isEmpty() == false)
                    temp.right = st.pop();
                else
                    return false;
                
                if(st.isEmpty() == false)
                    temp.left = st.pop();
                else
                    return false;
                ++i;
                st.push(temp);
            }
            else
            {
                if(Character.isLetter(tempC))
                {
                    temp = new Node(postfixExpression.substring(i, i + 1));
                    ++i;
                    st.push(temp);
                }
                else if(Character.isDigit(tempC))
                {
                    int k;
                    for(k = i + 1; k < n; ++k)
                        if(postfixExpression.charAt(k) == ' ')
                            break;
                    temp = new Node(postfixExpression.substring(i, k));
                    i = k;
                    st.push(temp);
                }
            }
        }
        if(st.isEmpty())
            return false;
        tree = st.pop();
        return st.isEmpty();
    }
    private boolean infixToPostfix()
    {
        char c;
        stack = new Stack<>();
        int i, k, n;
        for(i = 0, k = 0, n = infixExpression.length(); i < n; ++i)
        {
            c = infixExpression.charAt(i);
            if (Character.isLetter(c))
            {
                if(checkFormat(i, n) == false)
                    return false;
                postfixExpression += c + " ";
                ++i;
            }
            else if( Character.isDigit(c) )
            {
                for(k = i + 1; k < n; ++k)
                    if( Character.isDigit(infixExpression.charAt(k)) == false)
                        break;
                postfixExpression += infixExpression.substring(i, k) + " ";
                if(k < n)
                    if(infixExpression.charAt(k) != ' ')
                        return false;
                i = k;
            }
            else if(c == '(')
                stack.push(c);
            else if(c == ')')
            {
                if( checkFormat(i, n) == false )
                    return false;
                while(stack.isEmpty() == false)
                    if(stack.peek() != '(')
                        postfixExpression += stack.pop() + " ";
                    else 
                    	break;
                if(stack.isEmpty() == false)
                    stack.pop();
                else
                    return false;
            }
            else if(operandCheck(c))
            {
                if(checkFormat(i, n) == false)
                    return false;
                while(stack.isEmpty() == false)
                    if(precedence(c) > precedence(stack.peek()) )
                        break;
                    else
                        postfixExpression += stack.pop() + " ";
                stack.push(c);
                ++i;
            }
        }
        while(stack.isEmpty() == false)
            postfixExpression += " " + stack.pop();
        return true;
    }
    
    private boolean checkFormat(int i, int n)
    {
        if(i > 0)
            if(infixExpression.charAt(i - 1) != ' ')
                return false;
        if(i + 1 != n)
            if(infixExpression.charAt(i + 1) != ' ')
                return false;
        return true;
    }
    private boolean operandCheck(char c)
    {
    	switch (c)
        {
    		case '+':
    		case '-':
    		case '*':
    		case '/':
            case '^':
            	return true;
            default:
                return false;
        }
    }
    
    private int precedence(char c)
    {
        switch (c)
        {
            case '+':
            case '-':
                return 1;
      
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }
}