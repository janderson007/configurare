package configurare

import com.typesafe.scalalogging.StrictLogging
import sun.util.logging.resources.logging

import scala.reflect.io.AbstractFile
import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.shell.ReplReporterImpl
import scala.tools.nsc.interpreter.{IMain, ReplReporter, ReplRequest, Results}
import scala.reflect.internal.util.BatchSourceFile

object ConfigLoader extends StrictLogging {

  lazy val eval = {
    val settings = new Settings
    settings.usejavacp.value = true
    settings.deprecation.value = true
    new IMain(settings, new ReplReporterImpl(settings))
  }

  /** Loads the default configuration if specified via system properties */
  def get[T: Manifest](): Option[T] = {
    sys.props.get("configurare").flatMap { config =>
      get[T](config)
    }
  }

  def get[T: Manifest](name: String): Option[T] = {
   eval.interpret(name) match {
     case Results.Success => eval.valueOfTerm(eval.definedTerms.last.toString).map(_.asInstanceOf[T])
     case Results.Incomplete =>
       logger.warn("Incomplete or partial lookup specified")
       None
     case Results.Error =>
       logger.error(s"Unable to lookup ${name}")
       None
   }
  }

  def initRoot(): Unit = {
    sys.props.get("configurare.root").map { defaultRoot =>
      logger.debug(s"Initializing config with default root: ${defaultRoot}")
      initRoot(defaultRoot)
    } getOrElse {
      logger.warn("Unknown configuration root.  Specify default root with system property: -Dconfigurare.root=<path>")
      None
    }
  }

  /** Initialize from a source tree of configuration files */
  def initRoot(configPaths: String*): Unit = {
    import java.io.File
    def recursiveListFiles(f: File): Array[File] = {
      val these = f.listFiles
      these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
    }

    val scalaFiles = configPaths.flatMap { path =>
      recursiveListFiles(new File(path))
    }
      .filter( f => """.*\.scala$""".r.findFirstIn(f.getName).isDefined)
      .map { f =>
        val config = AbstractFile.getFile(f)
        logger.debug(s"Adding config file: ${f.getAbsolutePath}")
        new BatchSourceFile(config, config.toCharArray)
      }
    eval.compileSources(scalaFiles:_*)
  }

  /** Initializae from a list of specific files */
  def init(configPaths: String*): Unit= {
    val sourceFiles = configPaths.map { path =>
      val config = AbstractFile.getFile(path)
      new BatchSourceFile(config, config.toCharArray)
    }

   eval.compileSources(sourceFiles:_*)
  }
}
