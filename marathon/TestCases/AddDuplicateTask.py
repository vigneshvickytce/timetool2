useFixture(default)

def test():
	java_recorded_version = '1.6.0_02'

	if window('TimeTool'):
		click('add')

		if window('Add a Task'):
			select('TextField', 'NewTas')
			select('TextField1', 'A new task')
			keystroke('TextField1', 'Enter')
		close()
		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 0, 0.00], [Admin, Administrative time, 0, 0.00], [Lunch, Lunch, 0, 0.00], [NewTas, A new task, 0, 0.00]]')

		click('add')

		if window('Add a Task'):
			select('TextField', 'NewTas')
			select('TextField1', 'A new Task')
			click('OK')
		close()

		if window('Information'):
			assert_p('OptionPane.label', 'Text', 'A task with this ID already exists.')
			click('OK')
		close()

		select('taskTable', 'cell:Description,3(A new task)')
		click('delete')

		if window('Confirm Delete'):
			assert_p('OptionPane.label', 'Text', 'Are you sure you want to delete the selected task?')
			click('Yes')
		close()

		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 0, 0.00], [Admin, Administrative time, 0, 0.00], [Lunch, Lunch, 0, 0.00]]')

	close()
