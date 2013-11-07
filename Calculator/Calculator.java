/** 
 * HW1 Calculator
 * Write a program that reads an expression as input and print out the result.
 * Only Doubles and operators below are allowed in the expression:
 * + - * / % ( )
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;

public class Calculator {

	/**
	 * main 方法
	 * @param args
	 */
	public static void main(String[] args) {

		// 创建 Scanner
		Scanner input = new Scanner(System.in);

		// 所需对象
		String stringExp;
		Postfix postfixExp;
		double value;
		
		while( true ) {
			// 获取表达式字符串
			System.out.print("Input exp>");
			stringExp = input.nextLine();
			
			if(stringExp.equals("")){
				break;
			}
			
			// 创建后缀表达式
			try {
				postfixExp = new Postfix(stringExp);
			} catch (Throwable ex) {
				System.out.println("There's error in your input.");
				continue;
			}

			// 得到并输出表达式的值
			try {
				value = postfixExp.getValue();
			} catch (Throwable ex) {
				System.out.println("There's error in your input.");
				continue;
			}
			System.out.println("=" + value);
		}

		input.close();
		

		
	}
}

/**
 * 中缀表达式类
 * @author tgmerge
 * 可以从字符串构建中缀表达式。
 * 能处理负号和空格分隔符。
 */
class Infix {
	
	// 保存表达式的各项
	ArrayList<Object> elements = new ArrayList<Object>();

	/**
	 * 从字符串构造Infix对象
	 * @param string 表达式字符串
	 */
	public Infix(String string) {
		splitString(string);
		processMinus();
	}

	/**
	 * 得到表达式的某个元素（可能是String或Double）
	 * @param index
	 * @return 元素
	 */
	public Object getElement(int index) {
		return elements.get(index);
	}
	
	/**
	 * 返回表达式元素个数
	 * @return 元素个数
	 */
	public int getElementNum() {
		return elements.size();
	}
	
	/**
	 * 分割字符串为各个元素
	 * @param string 表达式字符串
	 * @return 如果成功，true 失败返回false
	 */
	private boolean splitString(String string) {
		
		// Remove all space in the expression
		string = string.replace(" ", "");
		
		// Divide symbols and digits into single elements
		int i = 0;
		char c;
		String str;
		while(i < string.length()) {
			str = new String();
			c = string.charAt(i);
			if(c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' ) {
				str += c;
				i ++;
				elements.add(str);
			} else if(c >= '0' && c <= '9') {
				double value;
				for(value = 0; i < string.length() && string.charAt(i) >= '0' && string.charAt(i) <= '9'; i ++) {
					str += string.charAt(i);
				}
				value = Double.parseDouble(str);
				elements.add(value);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 处理负号
	 * 在表达式开头，以及(后的负号前添加0
	 */
	private void processMinus() {

		// 在表达式开头的-前加0
		if(elements.size() > 0
				&& elements.get(0) instanceof String
				&& ((String) elements.get(0)).equals("-")
		  ) {
			elements.add(0, new Double(0));
		}
		
		// 在(后的-前加0
		if(elements.size() > 1) {
			for(int i = 1; i < elements.size(); i ++) {
				Object obj1 = elements.get(i-1);
				Object obj2 = elements.get(i);
				if( obj1 instanceof String && obj2 instanceof String
						&& ((String) obj1).equals("(")
						&& ((String) obj2).equals("-")
				  ) {
					elements.add(i, new Double(0));
				}
			}
		}
		
		// 调试用
		this.debug();
	}
	

	private void debug() {
		/*for(int ii = 0; ii < elements.size(); ii ++)
		{
			System.out.println("[infix]"+elements.get(ii));	
		}*/
	}
}

/**
 * 后缀表达式类
 * @author tgmerge
 * 可从字符串或中缀表达式构造后缀表达式，并求值。
 */
class Postfix {

	ArrayList<Object> elements = new ArrayList<Object>();
	
	/**
	 * 从字符串构造后缀表达式
	 * 需要Infix类。
	 * @param string 表达式字符串
	 */
	public Postfix(String string) {
		this(new Infix(string));
	}


	/**
	 * 从中缀表达式构造后缀表达式
	 * @param infix 中缀表达式
	 */
	public Postfix(Infix infix) {
		infixToPostfix(infix);
	}
	
	/**
	 * 转换中缀表达式为后缀表达式
	 * @param infix 要转换的表达式
	 * @return 成功与否
	 */
	private boolean infixToPostfix(Infix infix) {
		// 算法所需的栈和临时元素
		Stack<Object> stack = new Stack<Object>();
		Object e = new Object();
		Object p = new Object();
		
		// 顺序扫描表达式
		for(int i = 0; i < infix.getElementNum(); i ++) {
			// 从中缀取一个元素
			e = infix.getElement(i);
			
			// 如果是数字，则直接输出该数字
			if( e instanceof Double ) {
				elements.add(e);
			// 若为'('，直接入栈
			} else if(((String) e).equals("(")) {
				stack.push(e);
			// 若为')'，出栈并顺序输出运算符直到遇到第一个'(',遇到的第一个'('出栈但不输出
			} else if(((String) e).equals(")")) {
				for( p = stack.pop(); !(((String) p).equals("(")); p = stack.pop() ) {
					elements.add(p);
				}
			// 若为其它，比较运算符栈栈顶元素与当前元素的优先级：
			} else {
				if(! stack.empty()) {
					p = stack.peek();
				} else {
					p = new String("-");
				}
				// 如果栈顶元素是'('，当前元素直接入栈
				if(((String) p).equals("(")) {
					stack.push(e);
				//如果栈顶元素优先级<当前元素优先级，当前元素直接入栈。
				} else if( (((String) e).equals("*") || ((String) e).equals("/"))
						&& (((String) p).equals("+") || ((String) p).equals("-")) 
				       ) {
					stack.push(e);
				// 如果栈顶元素优先级>=当前元素优先级，出栈并顺序输出运算符直到栈顶元素优先级<当前元素优先级，然后当前元素入栈；
				} else {
					while ( (((String) e).equals("*") || ((String) e).equals("/"))
					     && (((String) p).equals("+") || ((String) p).equals("-")) == false || (!stack.empty()) ) {
						p = stack.pop();
						elements.add(p);
					}
					stack.push(e);
				}
			}
		}
		
		
		// 顺序出栈并输出运算符直到栈元素为空。
		while (stack.empty() == false) {
			p = stack.pop();
			elements.add(p);
		}
		
		debug();
		
		return true;
	}

	/** Calculate the value of a postfix exp */
	public double getValue() {
		// 算法所需的栈和临时元素
		Stack<Object> stack = new Stack<Object>();
		Object a, b;
		Object e;
		int i = 0;
		
		// 当有输入符号
		while(i < elements.size()) {
			// 读下一个符号
			e = elements.get(i);
			
			// 若是操作数，入栈
			if(e instanceof Double) {
				stack.push(e);
			} else {
				b = stack.pop();
				a = stack.pop();
				if( ((String)e).equals("+") ) {
					stack.push(new Double( (double)a + (double)b ));
				} else if( ((String)e).equals("-") ) {
					stack.push(new Double( (double)a - (double)b ));
				} else if( ((String)e).equals("*") ) {
					stack.push(new Double( (double)a * (double)b ));
				} else if( ((String)e).equals("/") ) {
					stack.push(new Double( (double)a / (double)b ));
				}
			}
			i ++;
		}
		
		return (double)stack.pop();
	}
	
	private void debug() {
		/*for(int ii = 0; ii < elements.size(); ii ++)
		{
			System.out.println("[postfix]"+elements.get(ii));	
		}*/
	}
	
}