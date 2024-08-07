package com.domker.doctor.base

object RefInvoke {
    /**
     * 根据全类名反射无参构造函数
     * 调用方式   Object obj = RefInvoke.createObject(className);
     *
     * @param className 全类名
     * @return
     */
    fun createObject(className: String?): Any? {
        val pareTypes = arrayOf<Class<*>>()
        val pareValues = arrayOf<Any>()
        try {
            val r = Class.forName(className!!)
            return createObject(r, pareTypes, pareValues)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据类反射无参构造函数
     * 调用方式   Object obj = RefInvoke.createObject(className);
     *
     * @param clazz 类
     * @return
     */
    fun createObject(clazz: Class<*>): Any? {
        val pareTyple = arrayOf<Class<*>>()
        val pareValues = arrayOf<Any>()
        return createObject(clazz, pareTyple, pareValues)
    }

    /**
     * 根据全类名（String）反射只有一个参数的构造函数
     *
     * @param className 类名
     * @param pareType  参数类型
     * @param pareValue 参数值
     * @return
     */
    fun createObject(className: String?, pareType: Class<*>, pareValue: Any): Any? {
        val pareTypes = arrayOf(pareType)
        val pareValues = arrayOf(pareValue)
        try {
            val r = Class.forName(className!!)
            return createObject(r, pareTypes, pareValues)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据类（Class）反射只有一个参数的构造函数
     *
     * @param clazz     类
     * @param pareTyple 参数类型
     * @param pareVaule 参数值
     * @return
     */
    fun createObject(clazz: Class<*>, pareTyple: Class<*>, pareVaule: Any): Any? {
        val pareTypes = arrayOf(pareTyple)
        val pareValues = arrayOf(pareVaule)
        return createObject(clazz, pareTypes, pareValues)
    }

    /**
     * 根据全类名（String）反射有多个参数的构造函数
     *
     * @param className  类名
     * @param pareTypes  参数类型数组
     * @param pareValues 参数值数组
     * @return
     */
    fun createObject(className: String?, pareTypes: Array<Class<*>>, pareValues: Array<Any>): Any? {
        try {
            val r = Class.forName(className!!)
            return createObject(r, pareTypes, pareValues)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据类（Class）反射有多个参数的构造函数
     *
     * @param clazz      类
     * @param pareTypes  参数类型数组
     * @param pareValues 参数值数组
     * @return
     */
    fun createObject(clazz: Class<*>, pareTypes: Array<Class<*>>, pareValues: Array<Any>): Any? {
        try {
            val ctor = clazz.getDeclaredConstructor(*pareTypes)
            ctor.isAccessible = true
            return ctor.newInstance(*pareValues)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 调用实例方法，多个参数
     *
     * @param obj        根据类名获取的构造函数对象
     * @param methodName 方法名
     * @param pareTypes  参数类型
     * @param pareValues 参数值
     * @return
     */
    fun invokeInstanceMethod(obj: Any?, methodName: String?, pareTypes: Array<Class<*>>, pareValues: Array<Any>): Any? {
        if (obj == null) {
            return null
        }
        try {
            //调用一个private方法
            val method = obj.javaClass.getDeclaredMethod(methodName!!, *pareTypes) //在指定类中获取指定的方法
            method.isAccessible = true
            return method.invoke(obj, *pareValues)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 调用实例方法，一个参数
     *
     * @param obj        根据类名获取的构造函数对象
     * @param methodName 方法名
     * @param pareTyple  参数类型
     * @param pareVaule  参数值
     * @return
     */
    fun invokeInstanceMethod(obj: Any?, methodName: String?, pareTyple: Class<*>, pareVaule: Any): Any? {
        val pareTypes = arrayOf(pareTyple)
        val pareValues = arrayOf(pareVaule)
        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues)
    }

    /**
     * 调用实例方法，无参
     *
     * @param obj        根据类名获取的构造函数对象
     * @param methodName 方法名
     * @return
     */
    fun invokeInstanceMethod(obj: Any?, methodName: String?): Any? {
        val pareTypes = arrayOf<Class<*>>()
        val pareValues = arrayOf<Any>()
        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues)
    }

    /**
     * 调用静态方法，无参
     *
     * @param className   类名（String）
     * @param method_name 方法名
     * @return
     */
    fun invokeStaticMethod(className: String?, method_name: String?): Any? {
        val pareTypes = arrayOf<Class<*>>()
        val pareValues = arrayOf<Any>()
        return invokeStaticMethod(className, method_name, pareTypes, pareValues)
    }

    /**
     * 调用静态方法，一个参数
     *
     * @param className   类名（String）
     * @param method_name 方法名
     * @param pareTyple   参数类型
     * @param pareVaule   参数值
     * @return
     */
    fun invokeStaticMethod(className: String?, method_name: String?, pareTyple: Class<*>, pareVaule: Any): Any? {
        val pareTypes = arrayOf(pareTyple)
        val pareValues = arrayOf(pareVaule)
        return invokeStaticMethod(className, method_name, pareTypes, pareValues)
    }

    /**
     * 用静态方法，多个参数
     *
     * @param className   类名（String）
     * @param method_name 方法名
     * @param pareTypes   参数类型[]
     * @param pareValues  参数值[]
     * @return
     */
    fun invokeStaticMethod(className: String?, method_name: String?, pareTypes: Array<Class<*>>, pareValues: Array<Any>): Any? {
        try {
            val obj_class = Class.forName(className!!)
            return invokeStaticMethod(obj_class, method_name, pareTypes, pareValues)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 调用静态方法，无参(Class)
     *
     * @param clazz       类
     * @param method_name 方法名
     * @return
     */
    fun invokeStaticMethod(clazz: Class<*>, method_name: String?): Any? {
        val pareTypes = arrayOf<Class<*>>()
        val pareValues = arrayOf<Any>()
        return invokeStaticMethod(clazz, method_name, pareTypes, pareValues)
    }

    /**
     * 调用静态方法，一个参数(Class)
     *
     * @param clazz       类
     * @param method_name 方法名
     * @param classType   参数类型
     * @param pareVaule   参数值
     * @return
     */
    fun invokeStaticMethod(clazz: Class<*>, method_name: String?, classType: Class<*>, pareVaule: Any): Any? {
        val classTypes = arrayOf(classType)
        val pareValues = arrayOf(pareVaule)
        return invokeStaticMethod(clazz, method_name, classTypes, pareValues)
    }

    /**
     * 调用静态方法，多个参数(Class)
     *
     * @param clazz       类
     * @param method_name 方法名
     * @param pareTypes   参数类型[]
     * @param pareValues  参数值[]
     * @return
     */
    fun invokeStaticMethod(clazz: Class<*>, method_name: String?, pareTypes: Array<Class<*>>, pareValues: Array<Any>): Any? {
        try {
            val method = clazz.getDeclaredMethod(method_name!!, *pareTypes)
            method.isAccessible = true
            return method.invoke(null, *pareValues)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取实例字段，简写版本
     *
     * @param obj       根据类名获取的构造函数对象
     * @param filedName 字段名
     * @return
     */
    fun getFieldObject(obj: Any, filedName: String?): Any? {
        return getFieldObject(obj.javaClass, obj, filedName)
    }

    /**
     * 获取实例字段
     *
     * @param className 类名
     * @param obj       根据类名获取的构造函数对象
     * @param filedName 字段名
     * @return
     */
    fun getFieldObject(className: String?, obj: Any?, filedName: String?): Any? {
        try {
            val obj_class = Class.forName(className!!)
            return getFieldObject(obj_class, obj, filedName)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取实例字段
     *
     * @param clazz     类
     * @param obj       根据类名获取的构造函数对象
     * @param filedName 字段名
     * @return
     */
    fun getFieldObject(clazz: Class<*>, obj: Any?, filedName: String?): Any? {
        try {
            val field = clazz.getDeclaredField(filedName!!)
            field.isAccessible = true
            return field[obj]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 设置实例字段，简写版本
     *
     * @param obj        根据类名获取的构造函数对象
     * @param filedName  字段名
     * @param filedVaule 字段值
     */
    fun setFieldObject(obj: Any, filedName: String?, filedVaule: Any?) {
        setFieldObject(obj.javaClass, obj, filedName, filedVaule)
    }

    /**
     * 设置实例字段
     *
     * @param clazz      类
     * @param obj        根据类名获取的构造函数对象
     * @param filedName  字段名
     * @param filedVaule 字段值
     */
    fun setFieldObject(clazz: Class<*>, obj: Any?, filedName: String?, filedVaule: Any?) {
        try {
            val field = clazz.getDeclaredField(filedName!!)
            field.isAccessible = true
            field[obj] = filedVaule
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 设置实例字段
     *
     * @param className  类名（String）
     * @param obj        根据类名获取的构造函数对象
     * @param filedName  字段名
     * @param filedVaule 字段值
     */
    fun setFieldObject(className: String?, obj: Any?, filedName: String?, filedVaule: Any?) {
        try {
            val obj_class = Class.forName(className!!)
            setFieldObject(obj_class, obj, filedName, filedVaule)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    /**
     * 获取静态实例字段
     *
     * @param className 类名（String）
     * @param filedName 字段名
     * @return
     */
    fun getStaticFieldObject(className: String?, filedName: String?): Any? {
        return getFieldObject(className, null, filedName)
    }

    /**
     * 获取静态实例字段
     *
     * @param clazz     类（Class）
     * @param filedName 字段名
     * @return
     */
    fun getStaticFieldObject(clazz: Class<*>, filedName: String?): Any? {
        return getFieldObject(clazz, null, filedName)
    }

    /**
     * 设置静态实例字段
     *
     * @param classname  类名
     * @param filedName  字段名
     * @param filedValue 字段值
     */
    fun setStaticFieldObject(classname: String?, filedName: String?, filedValue: Any?) {
        setFieldObject(classname, null, filedName, filedValue)
    }

    /**
     * 设置静态实例字段
     *
     * @param clazz      类
     * @param filedName  字段名
     * @param filedValue 字段值
     */
    fun setStaticFieldObject(clazz: Class<*>, filedName: String?, filedValue: Any?) {
        setFieldObject(clazz, null, filedName, filedValue)
    }
}