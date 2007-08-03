package com.timeTool.ui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.AbstractButton;
import javax.swing.JFrame;

import junit.framework.TestCase;

public class CommonDialogTest extends TestCase {

	public void test_Button_Clicks() {
		final CommonDialogTest.CommonDialogMock dialog = new CommonDialogMock();

        dialog.getContentPane().add(dialog.getButtons());

		final AbstractButton okButton = (AbstractButton)getComponentNamed(dialog, "OKButton");
		final AbstractButton cancelButton = (AbstractButton)getComponentNamed(dialog, "CancelButton");

		okButton.doClick();
		cancelButton.doClick();

		assertTrue(dialog.onOKCalled);
		assertTrue(dialog.onCancelledCalled);
	}

	protected static Component getComponentNamed(final Component parent, final String name) {
		if (parent == null) throw new IllegalArgumentException("Null: parent");
		if (name == null) throw new IllegalArgumentException("Null: name");

		if (name.equals(parent.getName())) {return parent;}

		if (parent instanceof Container) {

			for (Component aChildren : ((Container)parent).getComponents()) {
				final Component child = getComponentNamed(aChildren, name);
				if (child != null) {
					return child;
				}
			}
		}
		return null;
	}


	private final class CommonDialogMock extends CommonDialog {
		private boolean onOKCalled = false;
		private boolean onCancelledCalled = false;

		CommonDialogMock() {
			super(new JFrame(), "CommonDialogMock", false);
		}


		@Override
		protected void onOK() throws Exception {
			onOKCalled = true;
		}


		@Override
		protected void onCancel() {
			onCancelledCalled = true;
		}

	}
}
