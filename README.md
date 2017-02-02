# JavaUtilCore

[![Lucifer](https://avatars2.githubusercontent.com/u/24820552?v=3&s=460)](https://github.com/LuciferWong/)

 This project includes a number of commonly used Java tools. Sub package as follows

  - priv.lucife.utils.core.annotation
  - priv.lucife.utils.core.base
  - priv.lucife.utils.core.chinese
  - priv.lucife.utils.core.classUtil
  - priv.lucife.utils.core.clone
  - priv.lucife.utils.core.datastructure
  - priv.lucife.utils.core.date
  - priv.lucife.utils.core.db
  - priv.lucife.utils.core.encrypt
  - priv.lucife.utils.core.file
  - priv.lucife.utils.core.image
  - priv.lucife.utils.core.io
  - priv.lucife.utils.core.math



JavaUtilCore a simple tool for development .  As [Lucifer] writes on the [JavaUtilCore site][project]

>To facilitate your use, this code uses the  Apache License, Version 2.0 


### priv.lucife.utils.core.annotation
Beta
>Signifies that a public API (public class, method or field) is subject to incompatible changes, or even removal, in a future release. An API bearing this annotation is exempt from any compatibility guarantees made by its containing library. Note that the presence of this annotation implies nothing about the quality or performance of the API in question, only the fact that it is not "API-frozen." 
It is generally safe for applications to depend on beta APIs, at the cost of some extra work during upgrades. However it is generally inadvisable for libraries (which get included on users' CLASSPATHs, outside the library developers' control) to do so.

UBTCompatible
>The presence of this annotation on a type indicates that the type may be used with the UtilsBaseToolkit, When applied to a method, the return type of the method is UBT compatible. It's useful to indicate that an instance created by factory methods has a UBT serializable type.

UBTDS 
>The presence of this annotation on a type indicates that the type may be used with the UtilsBaseToolkit Data Structure, When applied to a method, the return type of the method is UBTS compatible. It's useful to indicate that an instance created by factory methods has a UBTS serializable type.

UBTInCompatible
>The presence of this annotation on a method indicates that the method may not be used with the UtilsBaseToolkit(UBT), even though its type is annotated as UBTCompatible and accessible in UBT. They can cause UBT compilation errors or simply unexpected exceptions when used in UBT. 
Note that this annotation should only be applied to methods, fields, or inner classes of types which are annotated as UBTCompatible.


VisibleForTesting
>Annotates a program element that exists, or is more widely visible than otherwise necessary, only for use in test code.

### priv.lucife.utils.core.base
  - ArrayUtil
  - ConvertUtil
  - HexByteUtil
  - RegUtil
  - StringUtil
  - UUIDUtil
  - ValidatorUtil

### priv.lucife.utils.core.chinese
  - PinyinUtil
  - RMBUtil
  

### priv.lucife.utils.core.classUtil
  - ReflectUtil

### priv.lucife.utils.core.clone
  - CloneUtil
  

### priv.lucife.utils.core.datastructure
  - BinarySearchTree
  - CheckBalanceTree
  - CommonAncestorSearch
  - DirectedGraphPathCheck
  - DoubleArrayTrie
  - GraphNode
  - GraphSearch
  - Hannotower
  - MinBinaryTree
  - Node
  - Queue
  - QueueWith2Stack
  - Stack
  - StackCapacity
  - StacksSet
  - StackWithMin
  - TreeNode
  - TreeSearch
  

### priv.lucife.utils.core.date
  - DateUtil


### priv.lucife.utils.core.db
  - JdbcUtil


### priv.lucife.utils.core.encrypt
  - AESUtil
  - Base32Util
  - Base64Util
  - DESUtil
  - EncryptAndDecryptUtil
  - MD5Util
  - SecurityUtil

### priv.lucife.utils.core.file
  - CompressUtil
  - FileUtil

### priv.lucife.utils.core.image
  - ImageUtil

### priv.lucife.utils.core.io
  - HttpUtil
  - IOUtil
  - URLUtil

### priv.lucife.utils.core.math
  - BigDecimalUtil
  - MathUtil
  - RandomUtil


   [Lucifer]: <https://github.com/LuciferWong>
   [project]: <https://github.com/LuciferWong/JavaUtilCore>
   [email]: <href="mailto:462881925@qq.com"/>
   
