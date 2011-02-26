package sample.common.console;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Component;

import sample.common.entity.Identifiable;
import sample.common.entity.NameId;

@Component
public class ConsoleImpl implements Console {

	public static final String DEFAULT_PROMPT_STRING = ">";
	private String promptString = DEFAULT_PROMPT_STRING;
	
	public String getPromptString() {
		return promptString;
	}

	public void setPromptString(String promptString) {
		this.promptString = promptString;
	}

	
	@Override
	public void display(String... messages) {
		for (String line : messages) {
			System.out.println(line); // 機能一覧の表示
		}
	}
	
	@Override
	public boolean confirm(String message, String yes, String no) {
		while (true) {
			String input = accept("\n" + message + "\n[" + yes + "," + no + "]" + promptString);
			if (yes.equals(input)) return true;
			if (no.equals(input)) return false;
			
			display("\n" + yes + "か" + no + "を入力してください。");
		}
	}
	
	@Override
	public String accept(String message) {
		System.out.println(message);
		System.out.print(promptString);

		return doAcceptChars();
	}
	
	@Override
	public String accept(String message, ValidInput<String> validInput) {
		while (true) { // 正しく入力されるまでループ
			String input = accept(message); 
		
			try {
				if (validInput.isValid(input)) return input;
			} catch (Exception ex) {
				// TODO 例外処理の場所の検討
				// もともとのコードを動きを一旦保持。
				ex.printStackTrace();
			}
		}
	}
	
	@Override
	public int acceptInt(String message) {
		while (true) {
			try {
				String input = accept(message); 

				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public int acceptInt(String message, ValidInput<Integer> validInput) {
		while (true) { // 正しく入力されるまでループ
			int input = acceptInt(message); 
		
			try {
				if (validInput.isValid(input)) return input;
			} catch (Exception ex) {
				// TODO 例外処理の場所の検討
				// もともとのコードを動きを一旦保持。
				ex.printStackTrace();
			}
		}
	}

	
	@Override
	public long acceptLong(String message) {
		while (true) {
			try {
				String input = accept(message); 

				return Long.parseLong(input);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public long acceptLong(String message, ValidInput<Long> validInput) {
		while (true) { // 正しく入力されるまでループ
			long input = acceptLong(message); 
		
			try {
				if (validInput.isValid(input)) return input;
			} catch (Exception ex) {
				// TODO 例外処理の場所の検討
				// もともとのコードを動きを一旦保持。
				ex.printStackTrace();
			}
		}
	}

	
	@Override
	public Date acceptDate(String message) {
		return acceptDate(message, "yyyyMMdd");
	}

	
	@Override
	public Date acceptDate(String message, String format) {
		while (true) {
			String input = accept(message); 
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			try {
				return dateFormat.parse(input);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public String acceptFromNameIdList(List<? extends NameId<?>> selectList, String message) {
		String result = null;
		while (true) { 
			System.out.println(message);
			System.out.print(nameIdListToString(selectList) + promptString);
			
			result = doAcceptChars();
			if (isValidId(selectList, result)) {
				return result;
			}
		}
	}

	private boolean isValidId(List<? extends Identifiable<?>> idList, String id) {
		for (Identifiable<?> identifiable : idList) {
			if (id.equals(ObjectUtils.toString(identifiable.getId()))) {
				return true;
			}
		}
		
		return false;
	}
	
	
	private String nameIdListToString(List<? extends NameId<?>> nameIdList) {
		StringBuilder buff = new StringBuilder();
		for (NameId<?> partner : nameIdList) {
			buff.append(partner.getId() + " " + partner.getName());
			buff.append("\n");
		}
		
		buff.append(" ["); // IDリストの表示
		for (NameId<?> partner : nameIdList) {
			buff.append(partner.getId());
			buff.append(",");
		}
		buff.deleteCharAt(buff.length() - 1); // 末尾の","を削除
		buff.append("]");
		
		return buff.toString();
	}

	@Override
	public String acceptFromIdList(List<? extends Identifiable<?>> selectList,
			String message) {
		
		String result = null;
		while (true) { 
			System.out.println(message);
			System.out.print(idListToString(selectList) + promptString);
			
			result = doAcceptChars();
			if (isValidId(selectList, result)) {
				return result;
			}
		}
	}

	
	private String idListToString(List<? extends Identifiable<?>> idList) {
		StringBuilder buff = new StringBuilder();
		for (Identifiable<?> partner : idList) {
			buff.append(partner.getId());
			buff.append("\n");
		}
		
		buff.append(" ["); // IDリストの表示
		for (Identifiable<?> partner : idList) {
			buff.append(partner.getId());
			buff.append(",");
		}
		buff.deleteCharAt(buff.length() - 1); // 末尾の","を削除
		buff.append("]");
		
		return buff.toString();
	}

	@Override
	public String acceptFromList(List<String> selectList, String message) {
		String result = null;
		while (true) { 
			System.out.println(message);
			System.out.print(selectList + promptString);
			
			result = doAcceptChars();
			if (selectList.contains(result)) return result;
		}
	}
	
	
	/**
	 * キーボードからの入力受取り
	 * 
	 * @return 入力文字列
	 */
	private String doAcceptChars() {
		int c;
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader isr = new InputStreamReader(System.in,
					System.getProperty("file.encoding"));
			do {
				c = isr.read();
				if (c == '\n') {
					return sb.toString();
				} else if (c == '\r') {
					c = isr.read();
					if (c == '\n') {
						return sb.toString();
					} else {
						sb.append('\r');
						sb.append((char) c);
					}
				} else {
					sb.append((char) c);
				}
			} while (c != -1);
			
			isr.close();
		} catch (IOException e) {
			System.err.println("入力受取りエラー:" + e.getMessage());
		}
		
		return null;
	}
}
