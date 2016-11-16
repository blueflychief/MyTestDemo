package com.example.administrator.mytestdemo.util;

import android.text.TextUtils;
import android.util.Log;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.SystemPrinter;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class KLog {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";
    public static final String PARAM = "Param";
    public static final String NULL = "null";
    public static String TAG_DEFAULT = "KKUserLog";
    public static final String SUFFIX = ".java";

    public static final int JSON_INDENT = 4;   //层级嵌套空格

    public static final int V = 0x1;
    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;
    public static final int JSON = 0x7;
    public static final int XML = 0x8;

    private static boolean IS_SHOW_LOG = false;   //Log开关
    private static final int STACK_TRACE_INDEX = 5;
    private boolean mUseXLog=true;

    /**
     * 初始化KLog
     *
     * @param isShowLog  开关
     * @param defaultTag    默认TAG
     */
    public static void init(boolean isShowLog, String defaultTag) {
        IS_SHOW_LOG = isShowLog;
        TAG_DEFAULT = defaultTag;
    }

    public static void initXLog() {
        //        XLog.init(BuildConfig.DEBUG ? LogLevel.ALL : LogLevel.NONE);
        LogConfiguration config = new LogConfiguration.Builder()
                .tag("MY_TAG")                                         // 指定 TAG，默认为 "X-LOG"
                .t()                                                   // 允许打印线程信息，默认禁止
                .st(2)                                                 // 允许打印深度为2的调用栈信息，默认禁止
                .b()                                                   // 允许打印日志边框，默认禁止
//                .jsonFormatter(new MyJsonFormatter())                  // 指定 JSON 格式化器，默认为 DefaultJsonFormatter
//                .xmlFormatter(new MyXmlFormatter())                    // 指定 XML 格式化器，默认为 DefaultXmlFormatter
//                .throwableFormatter(new MyThrowableFormatter())        // 指定可抛出异常格式化器，默认为 DefaultThrowableFormatter
//                .threadFormatter(new MyThreadFormatter())              // 指定线程信息格式化器，默认为 DefaultThreadFormatter
//                .stackTraceFormatter(new MyStackTraceFormatter())      // 指定调用栈信息格式化器，默认为 DefaultStackTraceFormatter
//                .borderFormatter(new MyBoardFormatter())               // 指定边框格式化器，默认为 DefaultBorderFormatter
//                .addObjectFormatter(AnyClass.class,                    // 为指定类添加格式化器
//                        new AnyClassObjectFormatter())                 // 默认使用 Object.toString()
                .build();

        Printer androidPrinter = new AndroidPrinter();             // 通过 android.util.Log 打印日志的打印器
        Printer systemPrinter = new SystemPrinter();               // 通过 System.out.println 打印日志的打印器
        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
                .Builder("/sdcard/xlog/")                              // 指定保存日志文件的路径
                .fileNameGenerator(new DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
//                .backupStrategy(new MyBackupStrategy())                // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
//                .logFlattener(new MyLogFlattener())                    // 指定日志平铺器，默认为 DefaultLogFlattener
                .build();

        XLog.init(LogLevel.ALL,                                    // 指定日志级别，低于该级别的日志将不会被打印
                config,                                                // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
                androidPrinter,                                        // 添加任意多的打印器。如果没有添加任何打印器，会默认使用 AndroidPrinter
                systemPrinter,
                filePrinter);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }


    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }


    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }


    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }


    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }


    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    public static void xml(String xml) {
        printLog(XML, null, xml);
    }

    public static void xml(String tag, String xml) {
        printLog(XML, tag, xml);
    }


    private static void printLog(int type, String tagStr, Object... objects) {
        if (!IS_SHOW_LOG) {
            return;
        }
        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                printDefault(type, tag, headString + msg);
                break;
            case JSON:
                printJson(tag, msg, headString);
                break;
            case XML:
                printXml(tag, msg, headString);
                break;
        }
    }


    private static String[] wrapperContent(String tagStr, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }
        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        String tag = (tagStr == null ? className : tagStr);
        if (TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT;
        }
        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";

        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {
        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }

    /**
     * 打印JSON数据
     *
     * @param tag
     * @param msg
     * @param headString
     */
    public static void printJson(String tag, String msg, String headString) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(KLog.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(KLog.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLine(tag, true);
        message = headString + KLog.LINE_SEPARATOR + message;
        String[] lines = message.split(KLog.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        printLine(tag, false);
    }


    /**
     * 打印XML数据
     *
     * @param tag
     * @param xml
     * @param headString
     */
    public static void printXml(String tag, String xml, String headString) {
        if (xml != null) {
            xml = formatXML(xml);
            xml = headString + "\n" + xml;
        } else {
            xml = headString + KLog.NULL_TIPS;
        }

        printLine(tag, true);
        String[] lines = xml.split(KLog.LINE_SEPARATOR);
        for (String line : lines) {
            if (!isEmpty(line)) {
                Log.d(tag, "║ " + line);
            }
        }
        printLine(tag, false);
    }

    public static String formatXML(String inputXML) {
        try {
            Source xmlInput = new StreamSource(new StringReader(inputXML));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
        } catch (Exception e) {
            e.printStackTrace();
            return inputXML;
        }
    }


    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }


    public static void printDefault(int type, String tag, String msg) {
        int index = 0;
        int maxLength = 4000;
        int countOfSub = msg.length() / maxLength;
        if (countOfSub > 0) {
            for (int i = 0; i < countOfSub; i++) {
                String sub = msg.substring(index, index + maxLength);
                printSub(type, tag, sub);
                index += maxLength;
            }
            printSub(type, tag, msg.substring(index, msg.length()));
        } else {
            printSub(type, tag, msg);
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case KLog.V:
                Log.v(tag, sub);
                break;
            case KLog.D:
                Log.d(tag, sub);
                break;
            case KLog.I:
                Log.i(tag, sub);
                break;
            case KLog.W:
                Log.w(tag, sub);
                break;
            case KLog.E:
                Log.e(tag, sub);
                break;
            case KLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }
}
