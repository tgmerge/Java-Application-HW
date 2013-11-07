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
	 * main ����
	 * @param args
	 */
	public static void main(String[] args) {

		// ���� Scanner
		Scanner input = new Scanner(System.in);

		// �������
		String stringExp;
		Postfix postfixExp;
		double value;
		
		while( true ) {
			// ��ȡ���ʽ�ַ���
			System.out.print("Input exp>");
			stringExp = input.nextLine();
			
			if(stringExp.equals("")){
				break;
			}
			
			// ������׺���ʽ
			try {
				postfixExp = new Postfix(stringExp);
			} catch (Throwable ex) {
				System.out.println("There's error in your input.");
				continue;
			}

			// �õ���������ʽ��ֵ
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
 * ��׺���ʽ��
 * @author tgmerge
 * ���Դ��ַ���������׺���ʽ��
 * �ܴ����źͿո�ָ�����
 */
class Infix {
	
	// ������ʽ�ĸ���
	ArrayList<Object> elements = new ArrayList<Object>();

	/**
	 * ���ַ�������Infix����
	 * @param string ���ʽ�ַ���
	 */
	public Infix(String string) {
		splitString(string);
		processMinus();
	}

	/**
	 * �õ����ʽ��ĳ��Ԫ�أ�������String��Double��
	 * @param index
	 * @return Ԫ��
	 */
	public Object getElement(int index) {
		return elements.get(index);
	}
	
	/**
	 * ���ر��ʽԪ�ظ���
	 * @return Ԫ�ظ���
	 */
	public int getElementNum() {
		return elements.size();
	}
	
	/**
	 * �ָ��ַ���Ϊ����Ԫ��
	 * @param string ���ʽ�ַ���
	 * @return ����ɹ���true ʧ�ܷ���false
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
	 * ������
	 * �ڱ��ʽ��ͷ���Լ�(��ĸ���ǰ���0
	 */
	private void processMinus() {

		// �ڱ��ʽ��ͷ��-ǰ��0
		if(elements.size() > 0
				&& elements.get(0) instanceof String
				&& ((String) elements.get(0)).equals("-")
		  ) {
			elements.add(0, new Double(0));
		}
		
		// ��(���-ǰ��0
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
		
		// ������
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
 * ��׺���ʽ��
 * @author tgmerge
 * �ɴ��ַ�������׺���ʽ�����׺���ʽ������ֵ��
 */
class Postfix {

	ArrayList<Object> elements = new ArrayList<Object>();
	
	/**
	 * ���ַ��������׺���ʽ
	 * ��ҪInfix�ࡣ
	 * @param string ���ʽ�ַ���
	 */
	public Postfix(String string) {
		this(new Infix(string));
	}


	/**
	 * ����׺���ʽ�����׺���ʽ
	 * @param infix ��׺���ʽ
	 */
	public Postfix(Infix infix) {
		infixToPostfix(infix);
	}
	
	/**
	 * ת����׺���ʽΪ��׺���ʽ
	 * @param infix Ҫת���ı��ʽ
	 * @return �ɹ����
	 */
	private boolean infixToPostfix(Infix infix) {
		// �㷨�����ջ����ʱԪ��
		Stack<Object> stack = new Stack<Object>();
		Object e = new Object();
		Object p = new Object();
		
		// ˳��ɨ����ʽ
		for(int i = 0; i < infix.getElementNum(); i ++) {
			// ����׺ȡһ��Ԫ��
			e = infix.getElement(i);
			
			// ��������֣���ֱ�����������
			if( e instanceof Double ) {
				elements.add(e);
			// ��Ϊ'('��ֱ����ջ
			} else if(((String) e).equals("(")) {
				stack.push(e);
			// ��Ϊ')'����ջ��˳����������ֱ��������һ��'(',�����ĵ�һ��'('��ջ�������
			} else if(((String) e).equals(")")) {
				for( p = stack.pop(); !(((String) p).equals("(")); p = stack.pop() ) {
					elements.add(p);
				}
			// ��Ϊ�������Ƚ������ջջ��Ԫ���뵱ǰԪ�ص����ȼ���
			} else {
				if(! stack.empty()) {
					p = stack.peek();
				} else {
					p = new String("-");
				}
				// ���ջ��Ԫ����'('����ǰԪ��ֱ����ջ
				if(((String) p).equals("(")) {
					stack.push(e);
				//���ջ��Ԫ�����ȼ�<��ǰԪ�����ȼ�����ǰԪ��ֱ����ջ��
				} else if( (((String) e).equals("*") || ((String) e).equals("/"))
						&& (((String) p).equals("+") || ((String) p).equals("-")) 
				       ) {
					stack.push(e);
				// ���ջ��Ԫ�����ȼ�>=��ǰԪ�����ȼ�����ջ��˳����������ֱ��ջ��Ԫ�����ȼ�<��ǰԪ�����ȼ���Ȼ��ǰԪ����ջ��
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
		
		
		// ˳���ջ����������ֱ��ջԪ��Ϊ�ա�
		while (stack.empty() == false) {
			p = stack.pop();
			elements.add(p);
		}
		
		debug();
		
		return true;
	}

	/** Calculate the value of a postfix exp */
	public double getValue() {
		// �㷨�����ջ����ʱԪ��
		Stack<Object> stack = new Stack<Object>();
		Object a, b;
		Object e;
		int i = 0;
		
		// �����������
		while(i < elements.size()) {
			// ����һ������
			e = elements.get(i);
			
			// ���ǲ���������ջ
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