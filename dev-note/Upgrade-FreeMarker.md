# Make article content page static with freemarker

### Current implement

Front server request back server to get the content of article. 
Back server request database to get the data and return to front.
Then front server render the front page to generate the html.

But some web pages, for example, article content page, is not frequently changed.
No matter how many times we request for one article, we almost get same page.
But the repeated request will cost a lot of time.

### What we want

Since the page is almost not changed, and we don't expect it to be very interactive,  
we want make it static. When a repeated request coming, we just return this static page
instead of querying database. This can reduce waiting time when requesting for a page
almost not change.

## Adv & Disadvantages

**Advantages**:
1. Reduce user waiting time
2. Release pressure on database
3. Easy SOE (Search Engine Optimization)

**Disadvantages**:
1. Low interactive
2. No database support (or need high maintain cost)

## How to do it

**Tech**:
1. JSP (Java Server Page)
2. FreeMarker (Compatible with more technology stacks)
3. Thymeleaf (Spring Boot supported)
4. Velocity

## Staticalize

### 1.Process:
```text
Template Page  -|
                |==> Freemarker ==> Static HTML Page
Dynamic Object -|
```

### 2.Maven Import
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-freemarker</artifactId>
    <version>${springboot-freemarker}</version>
</dependency>
```

## Guide

### 1.Dir & File
1. mkdir _templates_ in _recourse_
2. create _xxx.ftl_
3. fill html tag in _xxx.ftl_, such as <head>, <body>, etc.

### 2.Base Syntax
1. Comment: `<#-- Comment content -->`
2. Expression: `${expr content}`
3. Plain Text
4. FreeMarker Instruction

### 3.Configuration
In `application.yml`. Most config is default.
```yml
  freemarker:
    content-type: text/html
    charset: UTF-8
    suffix: .ftl
    template-loader-path: classpath:/templates/
```

### 4.Usage
1. Create a *.ftl file, e.g. hello.ftl.
2. Using expression ${**} to assign an attribute.
```html
<html>
<head>
    <title>Test FreeMarker Template Page</title>
</head>
<body>
    <div>This is a test FreeMarker template page, we want to show ${text}</div>
</body>
</html>
```
3. Create a controller
4. Add function to map to the target page

```java
@RequestMapping("/fmc")
@Controller // Do not use rest controller
public class FreeMarkerController {
    @GetMapping("/hello")
    public String freemarkerTest(Model model){
        String testStr = "This string is coming from FreeMarkerController";
        
        model.addAttribute("text", testStr); // match with symbol in expression

        return "hello"; // route --> resource/templates/ and find hello.ftl
    }
}
```
5. Run server and test. Page show:
<img src="/home/lr/Pictures/Screenshot from 2022-03-23 11-01-53.png"/>

### 5.Syntax
Add a makeModel function to put some objects into the model
```java
private Model makeModel(Model model, T attr) {
    model.addAttribute(targetAttr, attr);
    return model;
}
```

Then using `${expression}` to get something from the attr. 
Suppose that attr has an integer field named `age`. 
```html
<div>Age of attr is ${attr.age}</div>
```

**For those fields in attr that can not be simply displayed on the page,
we can use some regulars to convert it to a string.**
```html
<div>E.g. 1.Field 'exist' is a Boolean field, using: ${attr.exist?string('Exist', 'Not Exist') to show something depending on the value of boolean</div>
<div>E.g. 2.Field 'birthday' is a Date field, using: ${attr.birthday?string('yyyy-MM-dd HH:mm:ss') to format string to a date</div>
<div>E.g. 3.Field 'obj' is another object, it has a field named 'num', using: ${attr.obj.num} to access it</div>
```

**For collections, we need use an instruction to display it.**

Suppose that attr contains a list named `goodsList`. It may contain another object.
```html
<div>
    <#list attr.goodsList as good>
        <div>
            <span>${good.name}</span>
            <span>${good.price}</span>
        </div>
    </#list>
</div>
```

If this object is a map named `goodsMap`
```html
<div>
    <#list attr.goodsMap?keys as k>
        <div>
            <span>${attr.goodsMap[k]}</span>
        </div>
    </#list>
</div>
```
And if this expression `${attr.goodsMap[k]}` still get an object, we can access its contains like `${attr.goodsMap[k].XXX}`

**For conditional display**, use `if` instruction. The syntax of the statement is similar with Java
```html
<dev>
    <#if good.name == 'ABC'>
        The name of good is ABC 
    </#if>
    <#if good.price >= 15>
        The price is more than $15 
    </#if>
</dev>
```

If the field is a boolean type, it can be used as a statement simply.
```html
<dev>
    <#if aBoolean>
        The 'aBoolean' is true
    </#if>
</dev>
```

### 6.To generate static page

**6.1 Set html output path in yml**
```yml
freemarker:
  html:
    target: <target path>
```

Then assign it to a local variable
```java
@Value("${freemarker.html.target}")
private static String fmHtmlTarget;
```

**6.2 Create interface for getting a static page**
```java
@ResponseBody
@GetMapping("/createHtml")
public String createHtml(Model model) throws IOException, TemplateException {
    // 1.Config for freemarker, specify file path.
    // 2.Get existed ftl file
    // 3.Get dynamic data from database
    // 4.Process model and template to generate static html file
    return "";
}
```

1. Config
```java
// String fmTempPath -> template file location 

Configuration cfg = new Configuration(Configuration.getVersion());
cfg.setDirectoryForTemplateLoading(new File(fmTempPath));
String filename = "hello";
```

2. Get ftl file
```java
Template tmp = cfg.getTemplate(filename + ".ftl", "utf-8");
```

3. Get data
```java
String data = XXXX
model.addAttribute("text", data); 
```

4. Process and generate
```java
File target = new File(fmTempPath);
if (!target.exists()) {
    target.mkdirs();
}

Writer out = new FileWriter(target + File.separator + filename + ".html");
tmp.process(model, out);
out.close();
```
### 7.Decoupling
**Previous:**
Server: Generate static page ---> Delivery to front server.

**Decoupling:**
Server: Generate static page ---> Upload to file cloud storage (OSS, GridFS, etc.)

Front Server: Access to file cloud storage ---> Get static page ---> Download to front server


