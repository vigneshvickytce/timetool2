useFixture(default)

def test():
	java_recorded_version = '1.6.0_02'
	
	if window('TimeTool'):
		assert_p('Table', 'Content', '[[Defaul, Default task created by TimeTool, 0, 0.00], [Admin, Administrative time, 0, 0.00], [Lunch, Lunch, 0, 0.00]]')

		click('add')

		if window('Add a Task'):
			select('TextField', 'New Ta')
			select('TextField1', 'A new Task')
			click('OK')
		close()

		assert_p('Table', 'Content', '[[Defaul, Default task created by TimeTool, 0, 0.00], [Admin, Administrative time, 0, 0.00], [Lunch, Lunch, 0, 0.00], [New Ta, A new Task, 0, 0.00]]')

		select('Table', 'cell:Description,3(A new Task)')
		click('delete')

		if window('Confirm Delete'):
			click('Yes')
		close()

		assert_p('Table', 'Content', '[[Defaul, Default task created by TimeTool, 0, 0.00], [Admin, Administrative time, 0, 0.00], [Lunch, Lunch, 0, 0.00]]')

	close()
