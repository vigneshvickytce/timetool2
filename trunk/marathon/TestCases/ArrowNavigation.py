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
		keystroke('taskTable', 'Down')
		keystroke('taskTable', '+')
		keystroke('taskTable', 'Down')
		keystroke('taskTable', '+')
		keystroke('taskTable', 'Down')
		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 1, 0.02], [Admin, Administrative time, 1, 0.02], [Lunch, Lunch, 1, 0.02]]')
		keystroke('taskTable', '+')
		keystroke('taskTable', 'Up')
		keystroke('taskTable', '+')
		keystroke('taskTable', 'Up')
		keystroke('taskTable', '+')
		keystroke('taskTable', 'Up')
		assert_p('taskTable', 'Content', '[[Defaul, Default task created by TimeTool, 2, 0.03], [Admin, Administrative time, 2, 0.03], [Lunch, Lunch, 2, 0.03]]')

		click('reset')

		if window('Confirm Reset'):
			click('Yes')
		close()
	close()

