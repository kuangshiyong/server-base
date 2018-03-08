package cn.bjzfgcjs.idefense.common.utils;

import cn.bjzfgcjs.idefense.device.sound.sv2101.LCAudioThrDll;
import cn.bjzfgcjs.idefense.device.sound.sv2101.LCPlayback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinUser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.sun.jna.Native.getLastError;

// 创建Windows下隐形窗口，解决需要窗口数据的场景

@Service
public class JnaMsgWnd {

    private static final Logger logger = LoggerFactory.getLogger(JnaMsgWnd.class);

    private static Throwable exceptionInCreatedThread;

    public final static User32 user32 = User32.INSTANCE;

    @Resource
    private LCPlayback lcPlayback;


    public void createWindow(final String windowClass) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    createWindowAndLoop(windowClass);
                } catch (Throwable t) {
                    //will fail the test in case of exception in created thread.
                    exceptionInCreatedThread = t;
                }
            }
        }).start();
    }

    public void createWindowAndLoop(String windowClass) {
        // define new window class
        HMODULE hInst = Kernel32.INSTANCE.GetModuleHandle("");

        WNDCLASSEX wClass = new WNDCLASSEX();
        wClass.hInstance = hInst;

        wClass.lpfnWndProc = new WindowProc() {
            @Override
            public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam) {
                switch (uMsg) {
                    case LCAudioThrDll.WM_MSG_COMPLETED:
//                        lcPlayback.releaseResource(hwnd);
                        return new LRESULT(0);

                    default:
                        return User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
                }
            }
        };

        wClass.lpszClassName = windowClass;

        // register window class
        User32.INSTANCE.RegisterClassEx(wClass);
        getLastError();

        // create new window
        HWND hWnd = User32.INSTANCE.CreateWindowEx(User32.WS_EX_TOPMOST, windowClass,
                "hidden window", 0, 0, 0, 0, 0, null, null, hInst,
                null);
        getLastError();
        logger.info("window sucessfully created! window hwnd: {}", hWnd.getPointer().toString());

        MSG msg = new MSG();
        while (User32.INSTANCE.GetMessage(msg, hWnd, 0, 0) > 0) {
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
        }

        User32.INSTANCE.UnregisterClass(windowClass, hInst);
        User32.INSTANCE.DestroyWindow(hWnd);
    }

    public HWND getHWNDFromWindowClass(String windowClass) {
        CallBackFindWindowHandleByWindowclass cb = new CallBackFindWindowHandleByWindowclass(windowClass);

        User32.INSTANCE.EnumWindows(cb, null);

        return cb.getFoundHwnd();
    }

    private static class CallBackFindWindowHandleByWindowclass implements WNDENUMPROC {

        private HWND found;

        private String windowClass;

        public CallBackFindWindowHandleByWindowclass(String windowClass) {
            this.windowClass = windowClass;
        }

        @Override
        public boolean callback(HWND hWnd, Pointer data) {
            char[] windowText = new char[512];

            user32.GetClassName(hWnd, windowText, 512);
            String className = Native.toString(windowText);

            if (windowClass.equalsIgnoreCase(className)) {
                // Found handle. No determine root window...
                HWND hWndAncestor = user32.GetAncestor(hWnd, User32.GA_ROOTOWNER);
                found = hWndAncestor;
                return false;
            }
            return true;
        }

        public HWND getFoundHwnd() {
            return this.found;
        }

    }

}
