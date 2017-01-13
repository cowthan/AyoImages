AyoImageLoader是一个ImageLoader的顶层框架，主要目的是兼容UIL，Glide等框架，便于对比测试和切换

主要类是VanGogh

# 1 VanGogh用法

```java

//初始化
////配置三组占位图，大中小
VanGogh.initImageBig(R.drawable.loading_big);
VanGogh.initImageMiddle(R.drawable.loading_middle);
VanGogh.initImageSmall(R.drawable.loading_small);
VanGogh.init(this, Files.path.getPathInRoot2("uil"));

//加载图片
VanGogh.paper(full_image).paintBig(url);
VanGogh.paper(full_image).paintMiddle(url);
VanGogh.paper(full_image).paintSmall(url);

VanGogh.paper(full_image).imageEmpty(BIG_EMPTY)
                       .imageError(BIG_ERROR)
                       .imageLoading(BIG_LOADING)
                       .paint(url, listener, null);

//缓存路径：
String localCache = VanGogh.getLocalCachePath(url);

//

```


# 2 BitmapUtils

bitmap相关的工具类

# 3 如何兼容Glide，Fresco

Fresco是基于DraweeView，这个框架是兼容不了了
Glide，Piccaso等的兼容可以参考AyoSunny这个工程里的完全版ImageLoader框架