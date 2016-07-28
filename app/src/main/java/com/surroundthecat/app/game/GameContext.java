package com.surroundthecat.app.game;

import java.util.HashMap;

/**
 * Created by Jiang Meng on 2016/7/28.
 */
public class GameContext extends HashMap<String, Object> implements GameContains{

    // serialVersionUID 用来表明类的不同版本间的兼容性
    // 简单来说，Java的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致性的。
    // 在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体（类）的serialVersionUID进行比较，
    // 如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常。
    // 当实现java.io.Serializable接口的实体（类）没有显式地定义一个名为serialVersionUID，类型为long的变量时，
    // Java序列化机制会根据编译的class自动生成一个serialVersionUID作序列化版本比较用，
    // 这种情况下，只有同一次编译生成的class才会生成相同的serialVersionUID 。
    // 如果我们不希望通过编译来强制划分软件版本，即实现序列化接口的实体能够兼容先前版本，未作更改的类，
    // 就需要显式地定义一个名为serialVersionUID，类型为long的变量，
    // 不修改这个变量值的序列化实体都可以相互进行串行化和反串行化。

    private static final long serialVersionUID = 1L;
    public GameContext() {}

}
