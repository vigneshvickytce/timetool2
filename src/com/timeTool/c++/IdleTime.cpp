// IdleTime.cpp : Defines the entry point for the DLL application.
//

#include "stdafx.h"
#include "IdleTime.h"

#ifdef _MANAGED
#pragma managed(push, off)
#endif

BOOL APIENTRY DllMain( HMODULE hModule,
                       DWORD  ul_reason_for_call,
                       LPVOID lpReserved
					 )
{
    return TRUE;
}


JNIEXPORT jint JNICALL Java_com_timeTool_IdleJob_getIdleTime(JNIEnv *env, jobject o)
{
	DWORD idleTimeTicks;
	LASTINPUTINFO l;
	l.cbSize = sizeof(LASTINPUTINFO);
	GetLastInputInfo(&l); // get time in windows ticks since system start of last activity
	idleTimeTicks = GetTickCount() - l.dwTime; // compute idle time
	return (jint)(idleTimeTicks/1000); // returns idle time in seconds
}

#ifdef _MANAGED
#pragma managed(pop)
#endif

