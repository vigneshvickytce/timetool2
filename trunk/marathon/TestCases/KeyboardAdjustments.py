useFixture(default)

def test():
	java_recorded_version = '1.5.0_11'

	if window('TimeTool'):
		click('reset')

		if window('Confirm Reset'):
			click('Yes')
		close()

		select('taskTable', 'cell:Minutes,0(0)')
		keystroke('taskTable', '+')
		select('taskTable', 'cell:Minutes,1(0)')
		keystroke('taskTable', '+')
		select('taskTable', 'cell:Minutes,2(0)')
		keystroke('taskTable', '+')
		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 1, 0.02], [Admin, Administrative time, 1, 0.02], [Lunch, Lunch, 1, 0.02]]')

		select('taskTable', 'cell:Minutes,2(0)')
		keystroke('taskTable', '-')
		select('taskTable', 'cell:Minutes,1(0)')
		keystroke('taskTable', '-')
		select('taskTable', 'cell:Minutes,0(0)')
		keystroke('taskTable', '-')
		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 0, 0.00], [Admin, Administrative time, 0, 0.00], [Lunch, Lunch, 0, 0.00]]')

		select('taskTable', 'cell:Minutes,0(0)')
		keystroke('taskTable', '1')
		if window('Adjust Time'):
			keystroke('textField', '0')
			click('OK')
		close()

		select('taskTable', 'cell:Minutes,1(0)')
		keystroke('taskTable', '2')
		if window('Adjust Time'):
			keystroke('textField', '0')
			click('OK')
		close()

		select('taskTable', 'cell:Minutes,2(0)')
		keystroke('taskTable', '3')
		if window('Adjust Time'):
			keystroke('textField', '0')
			click('OK')
		close()

		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 10, 0.17], [Admin, Administrative time, 20, 0.33], [Lunch, Lunch, 30, 0.50]]')

		click('reset')

		if window('Confirm Reset'):
			click('Yes')
		close()
	close()
