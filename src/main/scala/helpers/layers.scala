import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

/**
 * Rendering is done layer by layer
 * Layer 0 - Background            : background
 * Layer 1 - BackgroundEffects     : area of effects, background effects
 * Layer 2 - GroundEntities        : ground entities
 * Layer 3 - GroundEntitiesEffects : area of effects, middle effects
 * Layer 4 - FlyingEntities        : flying entities
 * Layer 5 - FlyingEntitiesEffects : area of effects, high altitude effects
 * Layer 6 - PostProcessing        : post processing
 * Layer 7 - UserInterface         : user interface
 * Layer 8 - PrimaryUserInterface  : primary user interface
 * Layer 9 - GlobalPostProcessing  : global post processing
 */

object RenderLayers {
	private var backgroundImg            = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var backgroundEffectsImg     = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var groundEntitiesImg        = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var groundEntitiesEffectsImg = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var flyingEntitiesImg        = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var flyingEntitiesEffectsImg = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var postProcessingImg        = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var userInterfaceImg         = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var primaryUserInterfaceImg  = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)
	private var globalPostProcessingImg  = new BufferedImage(Cst.windowWidth, Cst.windowHeight, BufferedImage.TYPE_INT_ARGB)

	var background            : Graphics2D   =            backgroundImg.createGraphics
	var backgroundEffects     : Graphics2D   =     backgroundEffectsImg.createGraphics
	var groundEntities        : Graphics2D   =        groundEntitiesImg.createGraphics
	var groundEntitiesEffects : Graphics2D   = groundEntitiesEffectsImg.createGraphics
	var flyingEntities        : Graphics2D   =        flyingEntitiesImg.createGraphics
	var flyingEntitiesEffects : Graphics2D   = flyingEntitiesEffectsImg.createGraphics
	var postProcessing        : Graphics2D   =        postProcessingImg.createGraphics
	var userInterface         : Graphics2D   =         userInterfaceImg.createGraphics
	var primaryUserInterface  : Graphics2D   =  primaryUserInterfaceImg.createGraphics
	var globalPostProcessing  : Graphics2D   =  globalPostProcessingImg.createGraphics

	def resetRendering(width: Int, height: Int): Unit = {
		backgroundImg            = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		backgroundEffectsImg     = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		groundEntitiesImg        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		groundEntitiesEffectsImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		flyingEntitiesImg        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		flyingEntitiesEffectsImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		postProcessingImg        = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		userInterfaceImg         = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		primaryUserInterfaceImg  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
		globalPostProcessingImg  = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
	}

	def resetRendering(): Unit = {
		resetRendering(Cst.windowWidth, Cst.windowHeight)
	}

	def prepareRendering(): Unit = {
		background            =            backgroundImg.createGraphics
		backgroundEffects     =     backgroundEffectsImg.createGraphics
		groundEntities        =        groundEntitiesImg.createGraphics
		groundEntitiesEffects = groundEntitiesEffectsImg.createGraphics
		flyingEntities        =        flyingEntitiesImg.createGraphics
		flyingEntitiesEffects = flyingEntitiesEffectsImg.createGraphics
		postProcessing        =        postProcessingImg.createGraphics
		userInterface         =         userInterfaceImg.createGraphics
		primaryUserInterface  =  primaryUserInterfaceImg.createGraphics
		globalPostProcessing  =  globalPostProcessingImg.createGraphics
	}

	def closeRendering(): Unit = {
		background            .dispose
		backgroundEffects     .dispose
		groundEntities        .dispose
		groundEntitiesEffects .dispose
		flyingEntities        .dispose
		flyingEntitiesEffects .dispose
		postProcessing        .dispose
		userInterface         .dispose
		primaryUserInterface  .dispose
		globalPostProcessing  .dispose
	}

	def mergeLayers(g: Graphics2D) = {
		g.drawImage(           backgroundImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(    backgroundEffectsImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(       groundEntitiesImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(groundEntitiesEffectsImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(       flyingEntitiesImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(flyingEntitiesEffectsImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(       postProcessingImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage(        userInterfaceImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage( primaryUserInterfaceImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
		g.drawImage( globalPostProcessingImg, new AffineTransform(1, 0, 0, 1, 0, 0), null)
	}
}