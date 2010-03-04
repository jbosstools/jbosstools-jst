package org.jboss.tools.jst.jsp.outline.cssdialog.cssselector.viewers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class CSSSelectorTransfer extends ByteArrayTransfer {

	private static CSSSelectorTransfer instance = new CSSSelectorTransfer();
	private static final String TYPE_NAME = "selector-transfer-format"; //$NON-NLS-1$
	private static final int TYPEID = registerType(TYPE_NAME);

	private CSSSelectorTransfer() {

	}

	public static CSSSelectorTransfer getInstance(){
		return instance;
	}
	
	@Override
	protected int[] getTypeIds() {
		return new int[] { TYPEID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { TYPE_NAME };
	}

	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[]) super.nativeToJava(transferData);
		return fromByteArray(bytes);
	}

	@Override
	protected void javaToNative(Object object, TransferData transferData) {
		byte[] bytes = toByteArray((String[]) object);
		if (bytes != null)
			super.javaToNative(bytes, transferData);
	}

	protected byte[] toByteArray(String[] strings) {

		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(byteOut);

		byte[] bytes = null;

		try {
			/* write number of markers */
			out.writeInt(strings.length);

			/* write markers */
			for (int i = 0; i < strings.length; i++) {
				out.writeUTF(strings[i]);
			}
			out.close();
			bytes = byteOut.toByteArray();
		} catch (IOException e) {
			// when in doubt send nothing
		}
		return bytes;
	}

	protected String[] fromByteArray(byte[] bytes) {
		DataInputStream in = new DataInputStream(
				new ByteArrayInputStream(bytes));

		try {
			/* read number of gadgets */
			int n = in.readInt();
			/* read gadgets */
			String[] strings = new String[n];
			for (int i = 0; i < n; i++) {
				String string = in.readUTF();
				if (string == null) {
					return null;
				}
				strings[i] = string;
			}
			return strings;
		} catch (IOException e) {
			return null;
		}
	}

}
