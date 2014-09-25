package com.outspoken_kid.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.text.Editable;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.widget.EditText;

/**
 * v1.0.5
 * 
 * @author HyungGunKim
 * v1.0.5 - Edit isEmpty().
 * v1.0.4 - checkString() -> checkTextLength(), Add checkForbiddenContains().
 * v1.0.3 - Fix some bugs in checkString().
 * v1.0.2 - Add isEmpty(), checkString().
 * v1.0.1 - Add 'copyStringToClipboard'.
 */
public class StringUtils {

	/**
	 * For isEmpty.
	 */
	public static final int PASS = 0;
	public static final int FAIL_EMPTY = 2;
	public static final int FAIL_SHORT = 3;
	public static final int FAIL_LONG = 4;
	
	private static final char KOREAN_BEGIN_UNICODE = 44032; // 가 
	private static final char KOREAN_LAST_UNICODE = 55203; // 힣
	private static final char KOREAN_BASE_UNIT = 588;//각자음 마다 가지는 글자수
	
	//자음
	private static final char[] INITIAL_LETTER = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };
	
	public static boolean checkNullOrDefault(String _str, String _defaultString) {
		
		String defaultString = "default";
		
		if(_defaultString != null) {
			defaultString = _defaultString;
		}
		
		if(_str == null)
			return true;
		
		if(_str.equals(""))
			return true;
		
		if(_str.equals(defaultString))
			return true;
		
		return false;
	}
	
	public static String getFormattedNumber(int _num) {
		
		ArrayList<Character> arList = new ArrayList<Character>();
		
		String formattedNumber = "";
		
		String str = Integer.toString(_num);
		
		//lengthOfStr
		int los = str.length();
		int mod = los % 3;
		
		for(int i=0; i<los; i++) {
			arList.add(str.charAt(i));
		}
		
		for(int i=0; i<los; i++) {
			
			if(i%3 == mod && i != 0) {
				formattedNumber += ",";
			}
			
			formattedNumber += arList.get(i);
		}

		return formattedNumber;
	}
	
	public static String getFormattedNumber(long _num) {
		
		ArrayList<Character> arList = new ArrayList<Character>();
		
		String formattedNumber = "";
		
		String str = Long.toString(_num);
		
		//lengthOfStr
		int los = str.length();
		int mod = los % 3;
		
		for(int i=0; i<los; i++) {
			arList.add(str.charAt(i));
		}
		
		for(int i=0; i<los; i++) {
			
			if(i%3 == mod && i != 0) {
				formattedNumber += ",";
			}
			
			formattedNumber += arList.get(i);
		}

		return formattedNumber;
	}

	public static String convertUrl(String str) {
		
		try{
		    String url = new String(str.trim().replace(" ", "%20").replace("&", "%26")
		            .replace(",", "%2c").replace("(", "%28").replace(")", "%29")
		            .replace("!", "%21").replace("=", "%3D").replace("<", "%3C")
		            .replace(">", "%3E").replace("#", "%23").replace("$", "%24")
		            .replace("'", "%27").replace("*", "%2A").replace("-", "%2D")
		            .replace(".", "%2E").replace("/", "%2F").replace(":", "%3A")
		            .replace(";", "%3B").replace("?", "%3F").replace("@", "%40")
		            .replace("[", "%5B").replace("\\", "%5C").replace("]", "%5D")
		            .replace("_", "%5F").replace("`", "%60").replace("{", "%7B")
		            .replace("|", "%7C").replace("}", "%7D"));
		    
		    return url;
	    }catch(Exception e){
	        e.printStackTrace();
	    }
		return str;
	}

	/**
	 * 입력받은 문자열의 한글을 자음으로 변환해준다.
	 * 
	 * @param str : 변환할 문자열.
	 * @return : 변환된 문자열.
	 */
	public static String getInitailKoreanLetters(String str) {
		
		if(str == null || str.equals("")) {
			return null;
		}
		
		try {
			String initialString = "";
			
			int length = str.length();
			for(int i=0; i<length; i++) {
				
				char c = str.charAt(i);
				
				if(isKorean(c)) {
					initialString += getInitialKoreanLetter(str.charAt(i));
				} else {
					initialString += c;
				}
			}
			
			if(!initialString.equals("")) {
				return initialString;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static boolean isInitialKoreanLetter(char searchar){ 
		
		try {
			for(char c : INITIAL_LETTER){ 
				if(c == searchar){ 
					return true; 
				} 
			} 
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	 } 
		
	private static char getInitialKoreanLetter(char c) { 
	
		try {
			if(!isInitialKoreanLetter(c) && isKorean(c)) {
				int hanBegin = (c - KOREAN_BEGIN_UNICODE); 
				int index = hanBegin / KOREAN_BASE_UNIT; 
					
				return INITIAL_LETTER[index];
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		 
		return c;
	} 
		 
	 /**
	  * 해당 문자가 한글인지 검사
	  * @param c 문자 하나
	  * @return
	  */
	private static boolean isKorean(char c) {
		
		return isInitialKoreanLetter(c) || (KOREAN_BEGIN_UNICODE <= c && c <= KOREAN_LAST_UNICODE); 
	}

	public static boolean isEqualSinceFirst(String target, String key) {
		 
		if(target == null || key == null || target.length() < key.length()) {
			return false;
		}
		 
		try {
			String s1 = target.toUpperCase(Locale.getDefault());
			String s2 = key.toUpperCase(Locale.getDefault());
			 
			int length = s2.length();
			for(int i=0; i<length; i++) {

				char c1 = s1.charAt(i);
				char c2 = s2.charAt(i);
				 
				if(c1 != c2) {
					break;
				}
				 
				if(i == length - 1) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static boolean isEqualSinceFirstWithInitialLetters(String target, String key) {

		if(target == null || key == null || target.length() < key.length()) {
			return false;
		}
		 
		try {
			String s1 = target.toUpperCase(Locale.getDefault());
			String s2 = key.toUpperCase(Locale.getDefault());
			 
			int length = s2.length();
			for(int i=0; i<length; i++) {

				char c1 = s1.charAt(i);
				char c2 = s2.charAt(i);
				char c3 = '1';
				char c4 = '2';
				 
				if(isInitialKoreanLetter(c1) || isInitialKoreanLetter(c2)) {
					c3 = getInitialKoreanLetter(s1.charAt(i));
					c4 = getInitialKoreanLetter(s2.charAt(i));
				}
				 
				if(c1 != c2 && c3 != c4) {
					break;
				}
				 
				if(i == length - 1) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static boolean copyStringToClipboard(Context context, String text) {
		
		try {
			if (android.os.Build.VERSION.SDK_INT >= 11) {
				android.content.ClipboardManager clipboard 
					= (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText("label", text);
				clipboard.setPrimaryClip(clip);
		    } else {
		    	android.text.ClipboardManager clipboard 
				= (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		    	clipboard.setText(text);
		    }
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * Check the string whether forbidden or not.
	 * 
	 * @param editText : EditText to check.
	 * @param noAlphabet
	 * @param noKoreanLetters
	 * @param noNumber
	 * @param noOther
	 * @return Returns true when editText's string is forbidden, or not.
	 */
	public static boolean checkForbidContains(EditText editText, boolean noAlphabet,
			boolean noKoreanLetters, boolean noNumber, boolean noSpace, boolean noNewLine, boolean noOther) {
		
		try {
			String str = editText.getEditableText().toString();
			return checkForbidContains(str, noAlphabet, noKoreanLetters, noNumber, noSpace, noNewLine, noOther);
		} catch(Exception e) {
		}
		
		return false;
	}
	
	/**
	 * Check the string whether forbidden or not.
	 * 
	 * @param str : String to check.
	 * @param noAlphabet
	 * @param noKoreanLetters
	 * @param noNumber
	 * @param noOther
	 * 
	 * @return Returns true when 'str' is forbidden, or not.
	 */
	public static boolean checkForbidContains(String str, boolean noAlphabet, 
			boolean noKoreanLetters, boolean noNumber, boolean noSpace, boolean noNewLine, boolean noOther) {

		if(str == null || str.length() == 0) {
			return false;
		}
		LogUtils.log("check===================");
		
		try {
			/*
			 * Check all letters. 
			 */
			int size = str.length();
			for(int i=0; i<size; i++) {
				char c = str.charAt(i);
				
				//Korean.
				if(isKorean(c)) {
					if(noKoreanLetters) {
						LogUtils.log("hasKorean : " + c);
						return true;
					}
					
				//Alphabet.
				} else if((c >= 65 && c <= 90)
						|| (c >= 97 && c <= 122)) {
					if(noAlphabet) {
						LogUtils.log("hasAlphabet : " + c);
						return true;
					}

				//Number.
				} else if(c >= 48 && c <= 57) {
					if(noNumber) {
						LogUtils.log("hasNumber : " + c);
						return true;
					}

				//Space.
				} else if(c == 32) {
					if(noSpace) {
						LogUtils.log("hasSpace : " + c);
						return true;
					}
					
				//NewLine.
				} else if(c == 10) {
					if(noNewLine) {
						LogUtils.log("hasNewLine : " + c);
						return true;
					}
					
				//Other letter.
				} else {
					
					if(noOther) {
						LogUtils.log("hasOther : " + c);
						return true;
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		LogUtils.log("Pass.");
		
		return false;
	}
	
	public static boolean isEmpty(CharSequence str) {
		
		return str == null || str.length() == 0;
	}
	
	/**
	 * Check the string's length.
	 * 
	 * @param str
	 * @param minLength
	 * @param maxLength
	 * @return Result of checking.
	 */
	public static int checkTextLength(String str, int minLength, int maxLength) {

		try {
			if(str.length() < minLength) {
				return FAIL_SHORT;
			}
			
			if(str.length() > maxLength) {
				return FAIL_LONG;
			}
			
			if(isEmpty(str)) {
				return FAIL_EMPTY;
			}
			
			if((str == null || str.length() == 0) && !(minLength == 0)) {
				return FAIL_EMPTY;
			}
		} catch(Exception e) {
			return FAIL_EMPTY;
		}
		
		return PASS;
	}
	
	/**
	 * Check the string's length.
	 * 
	 * @param editable
	 * @param minLength
	 * @param maxLength
	 * @return Result of checking.
	 */
	public static int checkTextLength(Editable ed, int minLength, int maxLength) {

		try {
			String str = ed.toString();
			
			if(str.length() < minLength) {
				return FAIL_SHORT;
			}
			
			if(str.length() > maxLength) {
				return FAIL_LONG;
			}
			
			if(str.length() == 0 && !(minLength == 0)) {
				return FAIL_EMPTY;
			}
		} catch(Exception e) {
			return FAIL_EMPTY;
		}
		
		return PASS;
	}
	
	/**
	 * Check the string's length.
	 * 
	 * @param textView
	 * @param minLength
	 * @param maxLength
	 * @return Result of checking.
	 */
	public static int checkTextLength(EditText et, int minLength, int maxLength) {

		try {
			String str = et.getText().toString();
			
			if(str.length() < minLength) {
				return FAIL_SHORT;
			}
			
			if(str.length() > maxLength) {
				return FAIL_LONG;
			}
			
			if(str.length() == 0 && !(minLength == 0)) {
				return FAIL_EMPTY;
			}
		} catch(Exception e) {
			return FAIL_EMPTY;
		}
		
		return PASS;
	}

	public static String stringReplace(String str) {

		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		str = str.replaceAll(match, "");
		return str;
	}

	/**
	 * yyyy-MM-dd hh:mm 형식으로.
	 * 
	 * @param format
	 * @param timestamp
	 * @return
	 */
	public static String getDateString(String format, long timestamp) {
		
		try {
			Date date = new Date(timestamp);
			SimpleDateFormat dateformat = new SimpleDateFormat(format,
					Locale.getDefault());
			String dateString = dateformat.format(date);
			return dateString;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	     
		return null;
	}

	public static String objectToString(Serializable object) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    try {
	        new ObjectOutputStream(out).writeObject(object);
	        byte[] data = out.toByteArray();
	        out.close();

	        out = new ByteArrayOutputStream();
	        Base64OutputStream b64 = new Base64OutputStream(out, 0);
	        b64.write(data);
	        b64.close();
	        out.close();

	        return new String(out.toByteArray());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	public static Object stringToObject(String encodedObject) {
	    try {
	        return new ObjectInputStream(new Base64InputStream(
	                new ByteArrayInputStream(encodedObject.getBytes()), 0)).readObject();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
}
