from com.timeTool import TimeTool

class Fixture:
	def start_application(self):
		args = []
		TimeTool.main(args)

	def teardown(self):
		pass

	def setup(self):
		self.start_application()

